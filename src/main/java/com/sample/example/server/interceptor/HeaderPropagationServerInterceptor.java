package com.sample.example.server.interceptor;

import com.google.common.annotations.VisibleForTesting;
import com.sample.example.service.util.LogUtil;
import com.sample.example.service.util.ThreadUtil;
import io.grpc.*;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;

import java.util.UUID;
import java.util.logging.Logger;

public class HeaderPropagationServerInterceptor implements ClientInterceptor {

    LogUtil logUtil = new LogUtil();

    @VisibleForTesting
    static final Metadata.Key<String> X_REQUEST_ID =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        logUtil.logInfo("method="+method+" callOptions="+callOptions+" Context.current()"+ Context.current().attach());
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                /* put custom header */
                headers.put(X_REQUEST_ID, UUID.randomUUID().toString());
                super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        /**
                         * if you don't need receive header from server,
                         * you can use {@link io.grpc.stub.MetadataUtils#attachHeaders}
                         * directly to send header
                         */
                        logUtil.logInfo("header received from server:" + headers);
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
