package service.project;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import service.project.MsgRequest;
import service.project.MsgReply;
import service.project.BroadcastingLocationGrpc;

public class Service3Client {
    private final ManagedChannel channel;
    private final BroadcastingLocationGrpc.BroadcastingLocationStub asyncStub;
    
    
    public Service3Client(String host, int port, GUI gui) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = BroadcastingLocationGrpc.newStub(channel);
    }

    public void sendLocation(MsgRequest request, StreamObserver<MsgReply> responseObserver) {
        asyncStub.sendLocation(request, responseObserver);
    }
    
    public double getRandomLatitude() {
        double randomLatitude = generateRandomDouble(-90.0, 90.0);
        return randomLatitude;
    }

    public double getRandomLongitude() {
        double randomLongitude = generateRandomDouble(-180.0, 180.0);
        return randomLongitude;
    }

    public void startStreaming() {
        StreamObserver<MsgRequest> requestStreamObserver = asyncStub.sendLocation(new StreamObserver<MsgReply>() {
            @Override
            public void onNext(MsgReply msgReply) {
                
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error in streaming: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Streaming completed");
            }
        });
    }
    
    public void sendRandomLocation() {
        double randomLatitude = generateRandomDouble(-90.0, 90.0);
        double randomLongitude = generateRandomDouble(-180.0, 180.0);

        MsgRequest request = MsgRequest.newBuilder()
                .setLatitude(randomLatitude)
                .setLongitude(randomLongitude)
                .build();

        StreamObserver<MsgReply> responseObserver = new StreamObserver<MsgReply>() {
            @Override
            public void onNext(MsgReply msgReply) {
                // Handle server responses if needed.
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error in sending location: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Location sent to server");
            }
        };

        sendLocation(request, responseObserver);
    }
    
    // Generate random numbers
    private double generateRandomDouble(double min, double max) {
        return min + new Random().nextDouble() * (max - min);
    }
    
    // main
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                    Service3Client client = new Service3Client("localhost", 50053, frame);
                    frame.service3Client = client;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
