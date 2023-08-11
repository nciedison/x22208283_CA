package service.project;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;


import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

public class Service2Server {
    private static final Logger logger = Logger.getLogger(Service2Server.class.getName());
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(50052)
                .addService(new AvailableSeatsImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Server shut down");
                Service2Server.this.stop();
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
        final Service2Server server = new Service2Server();
        server.start();
        server.blockUntilShutdown();
    }

    static class AvailableSeatsImpl extends AvailableSeatsGrpc.AvailableSeatsImplBase {

        @Override
        public void checkSeats(MsgRequest request, StreamObserver<MsgReply> responseObserver) {
            logger.info("Calling gRPC unary type (from the server side)");

            int availableSeats =RandomNum(0, 20);
            MsgReply reply = MsgReply.newBuilder()
                    .setAvailableSeats(availableSeats)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
        
        // Generate random numbers
        private int RandomNum(int min, int max) {
            if (min > max) {
                throw new IllegalArgumentException("Max must be bigger than min");
            }

            Random random = new Random();
            return random.nextInt(max - min + 1) + min;
        }
    }
}
