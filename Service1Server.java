package service.project;

import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Service1Server {
    private static final Logger logger = Logger.getLogger(Service1Server.class.getName());
    private Server server;

    private void start() throws IOException, InterruptedException {
        server = io.grpc.ServerBuilder.forPort(50051)
                .addService(new AlteredRouteNotificationImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Server will shut down");
                try {
                    Service1Server.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("Server will shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Service1Server server = new Service1Server();
        server.start();
        server.blockUntilShutdown();
    }
    
    // Display route 1-4
    static class AlteredRouteNotificationImpl extends AlteredRouteNotificationGrpc.AlteredRouteNotificationImplBase {
        @Override
        public void sendRouteInfo(MsgRequest request, StreamObserver<MsgReply> responseObserver) {
            int i = RandomNum(1, 4);
            String notification = " "  + i ;

            MsgReply reply = MsgReply.newBuilder().setMessage(notification).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
    
    // Generate random number
    public static int RandomNum(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Max must be bigger than min");
        }
        
        Random random = new Random();
        int randomNum = random.nextInt(max - min + 1) + min;
        return randomNum;
    }
}
