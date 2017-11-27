package com.sample.example.server;

import com.sample.example.server.interceptor.RequestLoggingInterceptor;
import com.sample.example.service.RecurringLengthCalcServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new RecurringLengthCalcServiceImpl()) // Register Services
                .intercept(new RequestLoggingInterceptor()) // Register Interceptors
                .build();
        // TODO - Monitoring Response Times via Interceptors.
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
