package service.project;

import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.awt.*;

public class Service2Client {
    private static final Logger logger = Logger.getLogger(Service2Client.class.getName());
    private final AvailableSeatsGrpc.AvailableSeatsBlockingStub blockingStub;
    private final ManagedChannel channel;

    public Service2Client(String host, int port, GUI gui) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = AvailableSeatsGrpc.newBlockingStub(channel);
        gui.setClient(this);
    }
    
    // checkSeats()
    public String checkSeats(String vehicleId) {
    	if (!isValid(vehicleId)) {
            System.err.println("Invalid vehicleId");
            return "Please check the Id again";
        }
        MsgRequest request = MsgRequest.newBuilder()
                .setVehicleId(vehicleId)
                .build();

        MsgReply response = blockingStub.checkSeats(request);

        int availableSeats = response.getAvailableSeats();
        System.out.println(availableSeats);
        return "" + availableSeats;
    }
    
    // check validation
    private boolean isValid(String vehicleId) {
        
        return vehicleId.equals("1111") || vehicleId.equals("2222") ||
               vehicleId.equals("3333") || vehicleId.equals("4444");
    }

    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                    Service2Client client = new Service2Client("localhost", 50052, frame);
                    frame.setClient(client); // Pass the client to the GUI
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}