/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.DatagramPacket;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nguyen.truong
 */
public class RendezvousServerTest {
    
    public RendezvousServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of serverOn method, of class RendezvousServer.
     */
    @Test
    public void testServerOn() {
        System.out.println("serverOn");
        int listeningPort = 0;
        RendezvousServer.serverOn(listeningPort);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class RendezvousServer.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        RendezvousServer instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerServiceMachine method, of class RendezvousServer.
     */
    @Test
    public void testRegisterServiceMachine() {
        System.out.println("registerServiceMachine");
        DatagramPacket packet = null;
        RendezvousServer instance = null;
        instance.registerServiceMachine(packet);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serviceRequest method, of class RendezvousServer.
     */
    @Test
    public void testServiceRequest() {
        System.out.println("serviceRequest");
        String cmd = "";
        RendezvousServer instance = null;
        instance.serviceRequest(cmd);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
