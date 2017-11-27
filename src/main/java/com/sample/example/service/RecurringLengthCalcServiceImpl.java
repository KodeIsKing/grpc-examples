package com.sample.example.service;


import com.sample.example.RecurringLengthCalcServiceGrpc;
import com.sample.example.LengthRequest;
import com.sample.example.LengthResponse;
import com.sample.example.server.interceptor.HeaderPropagationServerInterceptor;
import com.sample.example.service.util.LogUtil;
import com.sample.example.service.util.ThreadUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class RecurringLengthCalcServiceImpl extends RecurringLengthCalcServiceGrpc.RecurringLengthCalcServiceImplBase {
    LogUtil logUtil = new LogUtil();
    ThreadUtil threadUtil = new ThreadUtil();

    @Override
    public void getLength(
            LengthRequest request, StreamObserver<LengthResponse> responseObserver) {
        logUtil.logInfo("Server Received Request before sleep ="+ request.getNumber());
        threadUtil.threadSleep(1000);
        logUtil.logInfo("Server Received Request after sleep ="+ request.getNumber());
        
        LengthResponse response = null;
        if(request.getNumber() != null && request.getNumber().length() == 1){
            response = LengthResponse.newBuilder().setLength(1).build();
        }else {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                    .usePlaintext(true)
                    .build();
            try {
                RecurringLengthCalcServiceGrpc.RecurringLengthCalcServiceBlockingStub stub
                        = RecurringLengthCalcServiceGrpc.newBlockingStub(channel);
                response = stub
                        .withInterceptors(new HeaderPropagationServerInterceptor())
                        .getLength(LengthRequest.newBuilder()
                        .setNumber(request.getNumber().substring(1))
                        .build());
            } catch (Exception ex){
                System.out.println("Server Error for request="+request.getNumber()+" with="+ex.getLocalizedMessage()+" --- "+ex.getStackTrace()[0].toString());
            }
            if(response != null) {
                int totalLength = 1 + response.getLength();
                response = LengthResponse.newBuilder().setLength(totalLength).build();
            }
        }

        logUtil.logInfo("Server Response before wait ="+ request.getNumber());
        threadUtil.threadSleep(5000);
        logUtil.logInfo("Server Response after wait ="+ request.getNumber());

        //TODO - Error responses.
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}