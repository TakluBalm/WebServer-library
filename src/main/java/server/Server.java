package server;
import io.github.classgraph.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;

public class Server {

    ServerSocket socket;
    Thread master;
    boolean running;
    ClassInfoList controllerClasses;
    Map<Route, Invocation> mapper = new RouteMap();
	ExecutorService pool;
	Properties properties;

    class Master implements Runnable{
        @Override
        public void run() {
            try {
                while (running) {
                    try {
                        Socket clientSocket = socket.accept();
                        HTTPSocket clientHttpSocket = new HTTPSocket(clientSocket, properties.inactivityTimeout);
                        Worker servant = new Worker(clientHttpSocket);
                        pool.submit(servant);
                    }catch (SocketTimeoutException e){}
                }
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    class Worker implements Runnable{
        HTTPSocket clientSocket;
 
		public Worker(HTTPSocket clientsocket){
            this.clientSocket = clientsocket;
        }

        @Override
        public void run() {
			try{
                while(true) {
					Request r = clientSocket.tryRequest();
					if(r == null){
						throw new IOException();
					}
					Invocation invocation = mapper.get(r.route);
					if(invocation == null){
						clientSocket.sendResponse(new Response("1.1").setStatusCode(404));
						continue;
					}
					try{
						Response response = (Response)invocation.invoke(r);
						clientSocket.sendResponse(response);
					}catch(InvocationTargetException e){
						clientSocket.sendResponse(new Response("1.1").setStatusCode(500));
					}
                }
			}catch(IOException e){
				try{
					clientSocket.closeConnection();
				}catch(Exception ee){}
				return;
			}
        }
    }

	public Server(Properties properties){
		this.properties = properties;
	}

	public Server(){
		this.properties = new Properties();
	}

	public void updateProperties(Properties properties){
		throw new UnsupportedOperationException("Reloading with new propeties is not yet supported\n");
	}

    public void start() throws IOException {
        try {
            ScanResult sr = new ClassGraph().enableAllInfo().scan();
            sr.getAllClasses().forEach(classInfo -> {
                AnnotationInfo u = classInfo.getAnnotationInfo("server.Controller");
                if(u != null){
                    Class<?> annotatedClass = classInfo.loadClass();
                    Method[] classMethods = annotatedClass.getDeclaredMethods();

                    try {

                        Constructor<?> c = annotatedClass.getDeclaredConstructor();
                        Object object = c.newInstance();

                        for(Method callableMethod: classMethods){
                            if(callableMethod.isAnnotationPresent(MethodHandler.class)){
                                Controller controller = annotatedClass.getAnnotation(server.Controller.class);
                                MethodHandler handler = callableMethod.getAnnotation(server.MethodHandler.class);

								if(!Response.class.isAssignableFrom(callableMethod.getReturnType())){
									System.out.println(annotatedClass.getName()+": "+ callableMethod.getName() +" does not return proper type");
									continue;
								}
								Class<?>[] ptypes = callableMethod.getParameterTypes();
								if(ptypes.length != 1 || !ptypes[0].isAssignableFrom(Request.class)){
									System.out.println(annotatedClass.getName()+": "+ callableMethod.getName() +" does not accept proper parameters");
									continue;
								}

                                Route route = new Route(handler.method(), controller.URL());
                                Invocation invocation = new Invocation(object, callableMethod, route.Path);

                                mapper.put(route, invocation);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
            });
			pool = Executors.newFixedThreadPool(properties.poolSize);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
        socket = new ServerSocket(properties.port);
        running = true;
        master = new Thread(new Master());
        master.start();
    }

    public void stop() throws IOException, InterruptedException {
        running = false;
        master.join();
        socket.close();
    }

}