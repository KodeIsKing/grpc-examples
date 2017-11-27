package com.sample.example.client;

import com.sample.example.RecurringLengthCalcServiceGrpc;
import com.sample.example.LengthRequest;
import com.sample.example.LengthResponse;
import com.sample.example.client.interceptor.HeaderClientInterceptor;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class GrpcBlockingClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext(true)
                .build();

        RecurringLengthCalcServiceGrpc.RecurringLengthCalcServiceBlockingStub stub
                = RecurringLengthCalcServiceGrpc.newBlockingStub(channel)
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withInterceptors(new HeaderClientInterceptor());
        //TODO - Request Id.
//        for(int i = 0 ; i < 1000; i++) {
        try {
            // TODO - Try-catch to get the HTTP2 + gRPC Error codes?
            LengthResponse helloResponse = stub.getLength(LengthRequest.newBuilder()
                    .setNumber("1234567")
                    .build());
            System.out.println("Client " + helloResponse.getLength());
        }catch(Exception ex){
            System.out.println("Client Error "+ex.getLocalizedMessage()+" --- "+ex.getStackTrace());
        }
//        }

        channel.shutdown();
    }
}

