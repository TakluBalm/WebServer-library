package server;
import io.github.classgraph.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.util.HashMap;

public class Server {

    ServerSocket socket;
    Thread master;
    boolean running;
    ClassInfoList controllerClasses;
    class WhiteColonial implements Runnable{
        @Override
        public void run() {
            try {
                while (running) {
                    try {// wait for a client to connect
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

                HTTPSocket.Request newRequest = clientSocket.waitRequest();
                if(newRequest != null){
                    System.out.println(newRequest);
                    clientSocket.send("Valid HTTP");
                }else{
                    clientSocket.send("Invalid HTTP");
                }

                // close the client socket
                clientSocket.closeConnection();
            } catch (Exception e){
                System.out.println(
                        "Yee failed black nigger because of "+
                                e
                );
            }
        }
    }

    public void start() throws IOException {
        HashMap<Route, Invocation> mapper = new HashMap<Route, Invocation>();
        try {
            ScanResult sr = new ClassGraph().enableAllInfo().scan();
            sr.getAllClasses().forEach(classInfo -> {
                AnnotationInfo u = classInfo.getAnnotationInfo("server.Controller");
                if(u != null){
                    Class<?> annotatedClass = classInfo.loadClass();
                    Method[] classMethods = annotatedClass.getDeclaredMethods();

                    try {

                        Constructor<?> c = annotatedClass.getConstructor();
                        Object o = c.newInstance();

                        for(Method callableMethod: classMethods){
                            if(callableMethod.isAnnotationPresent(MethodHandler.class)){
                                Controller controller = annotatedClass.getAnnotation(server.Controller.class);
                                MethodHandler handler = callableMethod.getAnnotation(server.MethodHandler.class);
                                mapper.put(new Route(controller.URL(), handler.method()), new Invocation(o, callableMethod));
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            System.out.println(mapper);
            System.out.println(mapper.get(new Route("/prakhar", "GET")));
            mapper.get(new Route("/prakhar", "GET")).run(null);


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