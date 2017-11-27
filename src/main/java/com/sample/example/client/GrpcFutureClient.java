package com.sample.example.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.sample.example.RecurringLengthCalcServiceGrpc;
import com.sample.example.LengthRequest;
import com.sample.example.LengthResponse;
import com.sample.example.RecurringLengthCalcServiceGrpc;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GrpcFutureClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext(true)
                .build();

        /*
        Shows that the tomcat way of thread creations per request.
         */
        ListenableFuture<LengthResponse> helloResponse = null;
        for(int i =0 ; i < 100; i ++) {
            RecurringLengthCalcServiceGrpc.RecurringLengthCalcServiceFutureStub stub
                    = RecurringLengthCalcServiceGrpc.newFutureStub(channel)
                    .withDeadline(Deadline.after(3000000, TimeUnit.SECONDS));
            helloResponse = stub
                    .withCompression("gzip")
                    .getLength(LengthRequest.newBuilder()
                            .setNumber("10000000")
                            .build());
        }
        try {
            System.out.println("Client " + helloResponse.get().getLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }
}

