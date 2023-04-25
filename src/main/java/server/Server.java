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

                        BlackSlave servant = new BlackSlave(clientSocket);
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

        Socket clientSocket;
        public BlackSlave(Socket clientsocket){
            this.clientSocket = clientsocket;
        }
        @Override
        public void run() {
            try{
                System.out.println("Black nigga instantiated.");
                // create input and output streams for the client socket
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                // read data from the client and send a response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(outputStream, true);

                String inputLine = reader.readLine();
                while (inputLine != null) {
                    System.out.println("Received message: " + inputLine);
                    writer.println(inputLine);
                    inputLine = reader.readLine();
                }

                // close the client socket
                clientSocket.close();
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

                        Constructor c=annotatedClass.getConstructor();
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