package server;
import io.github.classgraph.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Map;

public class Server {

    ServerSocket socket;
    Thread master;
    boolean running;
    ClassInfoList controllerClasses;
    Map<Route, Invocation> mapper = new RouteMap();

    class WhiteColonial implements Runnable{
        @Override
        public void run() {
            try {
                while (running) {
                    try {
                        Socket clientSocket = socket.accept();
                        HTTPSocket clientHttpSocket = new HTTPSocket(clientSocket, 2000);
                        BlackSlave servant = new BlackSlave(clientHttpSocket);
                        Thread slave = new Thread(servant);
                        slave.start();
                        slave.join();

                    }catch (SocketTimeoutException e){}
                }
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    class BlackSlave implements Runnable{
        HTTPSocket clientSocket;
        public BlackSlave(HTTPSocket clientsocket){
            this.clientSocket = clientsocket;
        }
        @Override
        public void run() {
            try{
                System.out.println("Black nigga instantiated.");
                while(true) {
                    Request newRequest = clientSocket.waitRequest();
                    if (newRequest != null) {
                        System.out.println(newRequest);
                        String path = newRequest.getPath();
                        String method = newRequest.getMethod();
                        Invocation invocation = mapper.get(new Route(method, path));
                        if (invocation == null) {
                            Response response = new Response("1.1").setStatusCode(404);
                            clientSocket.sendResponse(response);
                        } else {
							Response r = (Response)invocation.invoke(newRequest);
							System.out.println(r.headerString());
                            clientSocket.sendResponse(r);
                        }
                    } else {
                        clientSocket.sendResponse(new Response("1.1").setStatusCode(400));
                    }
                    String val = newRequest.getHeaders().get("connection");
                    if(val == null || !val.equals("keep-alive")){
                        System.out.println("Breaking connection");
                        break;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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

                                Route route = new Route(handler.method(), controller.URL());
                                Invocation invocation = new Invocation(object, callableMethod, route.getParameterMask());

                                mapper.put(route, invocation);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
        socket = new ServerSocket(8080);
        running = true;
        socket.setSoTimeout(500);
        master = new Thread(new WhiteColonial());
        master.start();
    }

    public void stop() throws IOException, InterruptedException {
        running = false;
        master.join();
        socket.close();
    }

}