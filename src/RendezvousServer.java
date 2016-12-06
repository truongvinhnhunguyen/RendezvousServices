
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/*
COMMAND 1: <R><ID><IP:PORT>
Register service machine (9 bytes)
+ <R>:  1 byte with character 'R"
+ <ID>: 2 bytes interger represent service machine's ID
+ <IP:PORT>: 4 bytes private IP;2 bytes private port of service machine
REPLY: NO REPLY

Example: R 0x0001 0xC0 0xA8 0x00 0x01 0x022B means
Register service machine ID 1 with private IP:HOST 192.168.0.1:555

COMMAND 2: <S><ID><IP:PORT>
Request connection to a registered service machine
+ <S>:  1 byte with character 'S'
+ <ID>: 2 bytes interger represent service machine's ID
+ <IP:PORT>: 4 bytes private IP;2 bytes private port of request machine
REPLY:
To requester: <I><ID><IP1:PORT1><IP2:PORT2>
+ <I>:  1 byte with character 'I'
+ <ID>: 2 bytes interger represent service machine's ID
+ <IP1:PORT1>: 4 bytes private IP;2 bytes private port of service machine
+ <IP2:PORT2>: 4 bytes public IP;2 bytes public port of service machine

To service: <I><ID><IP1:PORT1><IP2:PORT2>
+ <I>:  1 byte with character 'I'
+ <ID>: 2 bytes interger represent requester (managed by Rendevous Server)
+ <IP1:PORT1>: 4 bytes private IP;2 bytes private port of requester
+ <IP2:PORT2>: 4 bytes public IP;2 bytes public port of requester

*/

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
    Static finction to start server
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
                
                buf = packet.getData();
                switch(buf[0]){
                    case 'R':
                        break;
                        
                    case 'S':
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
