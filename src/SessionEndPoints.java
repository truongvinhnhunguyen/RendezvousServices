/*
Session Endpoints data
*/
public class SessionEndPoints {
    private String m_privateIP = null;
    private String m_publicIP = null;
    
    private int m_privatePort = 0;
    private int m_publicPort = 0;
    
        
    public String getPrivateIP(){
        return m_privateIP;
    }
    
    public int getPrivatePort(){
        return m_privatePort;
    }
    
    public String getPublicIP(){
        return m_publicIP;
    }
    
    public int getPublicPort(){
        return m_publicPort;
    }
    
     public void setPrivateIP(String privateIP){
        m_privateIP = privateIP;
    }
    
    public void setPrivatePort(int privatePort){
        m_privatePort = privatePort;
    }
    
    public void setPublicIP(String publicIP){
        m_publicIP = publicIP;
    }
    
    public void setPublicPort(int publicPort){
        m_publicPort = publicPort;
    }
}
