package service.project;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class JmDnsServiceManager {
	private static JmDNS jmdns = null;

    public static void register(String service, int PortGrpc) throws InterruptedException {

        try {
            // Create a JmDNS instance
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            String GrpcLocation = InetAddress.getLocalHost().getHostAddress() + ":" + PortGrpc;

            // Register a service on default multicast DNS port 5353
            ServiceInfo infoService = ServiceInfo.create(service, GrpcLocation, 5353, "Location of gRPC service");
            jmdns.registerService(infoService);
            System.out.println("Service " + service + " registered");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() throws InterruptedException, IOException {
        jmdns.unregisterAllServices();
        jmdns.close();
    }
}