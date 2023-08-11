package service.project;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class Service1Client {
    private static final Logger logger = Logger.getLogger(Service1Client.class.getName());
    private final AlteredRouteNotificationGrpc.AlteredRouteNotificationStub asyncStub;
    private final ManagedChannel channel;
    private GUI gui;
    
    // Constructor
    public Service1Client(String host, int port, GUI gui) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = AlteredRouteNotificationGrpc.newStub(channel);
        this.gui = gui;
    }
    
    public ManagedChannel getChannel() {
        return channel;
    }
    
    public void onNotificationReceived(String notification) {
        SwingUtilities.invokeLater(() -> {
            if (!notification.isEmpty()) {
                gui.updateTextField(notification);
            }
        });
    }
    
    // Stream observer
    public void sendRouteInfo() {
        StreamObserver<MsgReply> responseObserver = new StreamObserver<MsgReply>() {
            @Override
            public void onNext(MsgReply value) {
            	// Generate RandomNumber
                int RandomNum = value.getAvailableSeats();
                System.out.println(RandomNum);

  
                SwingUtilities.invokeLater(() -> {
                    gui.updateTextField(String.valueOf(RandomNum));
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Notification stream completed");
            }
        };

        MsgRequest request = MsgRequest.newBuilder().build();

        asyncStub.sendRouteInfo(request, responseObserver);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GUI frame = new GUI();
                frame.setVisible(true);

                Service1Client client = new Service1Client("localhost", 50051, frame);
                client.sendRouteInfo();

                try {
                    client.channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Client shutdown interrupted", e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
