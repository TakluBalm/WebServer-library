package server;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.io.*;
import java.net.*;

public class Server {

    ServerSocket socket;
    Thread master;
    boolean running;
    ClassInfoList sub_BC;
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
                BaseController.RWK("nalla", "berozgar");

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
        try {
            ScanResult sr = new ClassGraph().enableAllInfo().scan();
            sr.getAllClasses().forEach(classInfo -> {
                if (classInfo.isStandardClass() && classInfo.isPublic() && !classInfo.isAbstract()&& classInfo.getName().equals("server.BaseController")){
                    sub_BC = classInfo.getSubclasses();
                    if(sub_BC.isEmpty()){
                        System.out.println("No subclasses of BaseController.");
                    }else{
                        int cnt = 0;
                        for(ClassInfo subclass: sub_BC){
//                            System.out.println(subclass.getAnnotations());
//                            System.out.println(subclass.getAnnotationInfo("Route"));
//                            System.out.println();

                            if((subclass.getAnnotationInfo("server.Route")!=null)) cnt++;
                        }
                        System.out.println(cnt);
                    }
                }

            });
        } catch (Exception e){

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