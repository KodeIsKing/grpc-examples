package com.sample.example.server.interceptor;

import com.google.common.annotations.VisibleForTesting;
import com.sample.example.service.util.LogUtil;
import io.grpc.*;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;

/**
 * A interceptor to handle server header.
 */
public class RequestLoggingInterceptor implements ServerInterceptor {
    LogUtil logUtil = new LogUtil();

    @VisibleForTesting
    static final Metadata.Key<String> X_REQUEST_ID =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);
//    @VisibleForTesting
//    static final Context.Key<String> X_REQUEST_ID_CONTEXT = Context.Key<String>("x-request-id");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            final Metadata requestHeaders,
            ServerCallHandler<ReqT, RespT> next) {
        logUtil.logInfo("call="+call+" headers="+requestHeaders);
//        Context.current().withValue(X_REQUEST_ID_CONTEXT, requestHeaders.get(X_REQUEST_ID));
        return next.startCall(call, requestHeaders);
    }
}
