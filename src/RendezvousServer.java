import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RendezvousServer implements Runnable {
    
    int m_listeningPort;
    DatagramSocket m_serverSocket = null;
    boolean m_isServing = true;
    
    static HashMap <Integer, SessionEndPoints> m_sessionTable;
    
    /*
    Constructor
    */
    RendezvousServer (int listeningPort){
        m_listeningPort = listeningPort;
    }
    
    /*
    Static function to start server
    */
    public static void serverOn(int listeningPort) {

        // Construct session table
        m_sessionTable = new HashMap();
        
        // Create ONE thread to process Datagram packet
        new Thread(new RendezvousServer(listeningPort)).start();
    }

    /*
    Thread to serve requesters or service machines
    */
    public void run() {
        try {
   
            m_serverSocket = new DatagramSocket(m_listeningPort);                       
            
            while (m_isServing) {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                
                // wait for command
                System.out.println("Wait client!");
                m_serverSocket.receive(packet);
                System.out.println("Command came!");
                String cmd = new String (packet.getData());
                
                switch(cmd.charAt(0)){
                    case 'R':
                        registerServiceMachine(packet);
                        break;
                        
                    case 'S':
                        serviceRequest(cmd);
                        break;
                        
                    case '0': // STOP SERVER
                        m_isServing = false;
                        break;
                        
                    default:
                        simpleTalk(packet);                           
                }           
            }
            // Server stop
            m_serverSocket.close();
            System.out.println("Bye!");
        } catch (IOException e) {
            System.out.println("serverOn: "+e);
        }
    }
    
   /*
    * --------------------------------------------------------------------------
    * void registerServiceMachine(String cmd)
    * --------------------------------------------------------------------------
    * COMMAND 1: "R:" + "<ID>:" + "<IP>:" + "<PORT>"
    * Register service machine.
    * Where,
    * + "R:": String of characters 'R' and separator ':'
    * + <ID>: An Interger.toSting represents service machine's ID
    * + <IP>: A String formated xxx.xxx.xxx.xxx represents PRIVATE IP of service
        machine.
    * + <PORT>: An Interger.toString represents PRIVATE PORT of service machine

    * REPLY: NO REPLY

    * Example: "R:1234:192.168.0.1:555"
    * --------------------------------------------------------------------------
    */
    void registerServiceMachine(DatagramPacket packet) {
        
        System.out.println("Service Machine registration");
       
        SessionEndPoints endPoints = new SessionEndPoints();
        String cmd = new String (packet.getData());
        
        String sTemp;
        int start;
        int end;
        
        // Get service machine's ID
        start = 2;
        end = cmd.indexOf(":", start);
        Integer serviceId = new Integer(cmd.substring(start, end-1));
        System.out.println("SERVICE ID: " + serviceId);
        
        // Get PRIVATE IP
        start = end + 1;
        end = cmd.indexOf(":", start);
        sTemp = cmd.substring(start, end-1);
        endPoints.setPrivateIP(sTemp);
        System.out.println("PRIVATE IP: " + sTemp);
        
        // Get PRIVATE PORT
        start = end + 1;
        end = cmd.length() - 1;
        sTemp = cmd.substring(start, end-1);
        endPoints.setPrivatePort(Integer.parseInt(sTemp));
        System.out.println("PRIVATE PORT: " + sTemp);
        
        // Get PUBLIC IP
        sTemp = packet.getAddress().toString();
        endPoints.setPublicIP(sTemp);
        System.out.println("PUBLIC IP: " + sTemp);
        
        // Get PUBLIC PORT
        int pPort = packet.getPort();
        endPoints.setPublicPort(pPort);
        System.out.println("PUBLIC PORT: " + pPort);
        
        // Put to session table. If key is existing, do replacing value
        m_sessionTable.put(serviceId, endPoints);
    }
    
    /*
    * --------------------------------------------------------------------------
    * void serviceRequest(String cmd)
    * --------------------------------------------------------------------------
    * COMMAND 2: "S:" + "<ID>:" + "<IP>:" + "<PORT>"
    * Request connection to a registered service machine
    * Where,
    * + "S:": String of characters 'S' and separator ':'
    * + <IP>: A String formated xxx.xxx.xxx.xxx represents PRIVATE IP of 
    *   service machine.
    * + <PORT>: An Interger.toString represents PRIVATE PORT of service machine
    *
    * REPLY:
    * To REQUESTER: "I:" + "<ID>:" + "<IP1>:" + "<PORT1>:" + "<IP2>:" + "<PORT2>"
    * Where,
    * + "I:": String of characters 'I' and separator ':'
    * + <ID>: 2 bytes interger represent service machine's ID
    * + <IP1>: A String formated xxx.xxx.xxx.xxx represents PRIVATE IP of 
    *   service machine.
    * + <PORT1>: An Interger.toString represents PRIVATE PORT of service machine.
    * + <IP2>: A String formated xxx.xxx.xxx.xxx represents PUBLIC IP of 
    *   service machine.
    * + <PORT2>: An Interger.toString represents PUBLIC PORT of service machine.
    *
    * To SERVICE: "I:" + "<ID>:" + "<IP1>:" + "<PORT1>:" + "<IP2>:" + "<PORT2>"
    * + "I:": String of characters 'I' and separator ':'
    * + <ID>: 2 bytes interger represent service machine's ID
    * + <IP1>: A String formated xxx.xxx.xxx.xxx represents PRIVATE IP of 
    *   requester.
    * + <PORT1>: An Interger.toString represents PRIVATE PORT of requester.
    * + <IP2>: A String formated xxx.xxx.xxx.xxx represents PUBLIC IP of 
    *   requester.
    * + <PORT2>: An Interger.toString represents PUBLIC PORT of requester.
    * --------------------------------------------------------------------------
    */
    void serviceRequest(String cmd) {
        
    }
    
    /*
    For testing purpose only
    Read data from client and send back client's IP address
    */
    private void simpleTalk(DatagramPacket packet){
        // Send something back to client
        try {
            byte[] buf = new byte[256];
            int len = 0;
            
            // Print data
            buf = packet.getData();
            len = packet.getLength();
            
            System.out.print("Data received: ");
            System.out.print(len);
            System.out.print(" byte(s): ");
            for(int i=0; i<len; i++)
                System.out.print((char)buf[i]);
            
            System.out.println();
            
            // Print IP
            byte[] clientAddr = new byte[4];
            clientAddr = packet.getAddress().getAddress();
            System.out.print("CLIENT IP: ");
            for (int i=0; i<4; i++) {
                System.out.print(clientAddr[i]&0xFF);
                
                if(i<3)
                    System.out.print(".");
            }
            System.out.println();
            
            // Print Port
            System.out.println("CLIENT PORT: "+packet.getPort());
            
            //-----------------------------------------------------------------
            // SEND SOMETHING BACK
            String bMsg = "HELLO IP/PORT: ";
            bMsg += packet.getAddress().toString();
            bMsg += "/";
            bMsg += packet.getPort();
            
            DatagramPacket bPacket = new DatagramPacket(bMsg.getBytes(), 
                          bMsg.length(), packet.getAddress(), packet.getPort());
            
            m_serverSocket.send(bPacket);
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
       
}
