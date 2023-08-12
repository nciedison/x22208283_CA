package service.project;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Service3Server {
    private static final Logger logger = Logger.getLogger(Service3Server.class.getName());
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(50053)
                .addService(new BroadcastingLocationImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Server shutting down...");
                Service3Server.this.stop();
                System.err.println("Server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Service3Server server = new Service3Server();
        server.start();
        server.blockUntilShutdown();
    }

    static class BroadcastingLocationImpl extends BroadcastingLocationGrpc.BroadcastingLocationImplBase {

        @Override
        public StreamObserver<MsgRequest> sendLocation(StreamObserver<MsgReply> responseObserver) {
            return new StreamObserver<MsgRequest>() {
                @Override
                public void onNext(MsgRequest msgRequest) {
                	
                    double randomLatitude = generateRandomLatitude();
                    double randomLongitude = generateRandomLongitude();

                    MsgReply reply = MsgReply.newBuilder()
                            .build();

                    responseObserver.onNext(reply);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error in streaming: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }

        private double generateRandomLatitude() {
            return new Random().nextDouble() * 180.0 - 90.0;
        }

        private double generateRandomLongitude() {
            return new Random().nextDouble() * 360.0 - 180.0;
        }
    }
}
