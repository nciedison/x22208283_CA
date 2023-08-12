package service.project;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class JmDnsServiceFind {
	private static ServiceInfo infoService = null;
	private static String GrpcLocation = "";
	private static JmDNS jmdns = null;

	private static class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
        	infoService = event.getInfo();
        	GrpcLocation = infoService.getName().split("_", 1)[0];
        	System.out.println("Service added: " + infoService);
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
        	infoService = event.getInfo();
        	GrpcLocation = infoService.getName().split("_", 1)[0];
        	System.out.println("Service removed: " + infoService);
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
        	infoService = event.getInfo();
        	GrpcLocation = infoService.getName().split("_", 1)[0];
        	System.out.println("Service resolved: " + infoService);
        }
    }

    public static void find(String service) throws InterruptedException {
        try {
            // Create a JmDNS instance
            jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Add a service listener
            jmdns.addServiceListener(service, new SampleListener());
            System.out.println("Listening services");

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getLocGrpc() {
    	return GrpcLocation;
    }
}