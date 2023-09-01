package com.ceit.desktop.grpc.caCenter;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.34.1)",
    comments = "Source: device.proto")
public final class CaCenterGrpc {

  private CaCenterGrpc() {}

  public static final String SERVICE_NAME = "caCenter.CaCenter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DevRegisterRequest,
      com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> getDeviceRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeviceRegister",
      requestType = com.ceit.desktop.grpc.caCenter.DevRegisterRequest.class,
      responseType = com.ceit.desktop.grpc.caCenter.DeviceRegisteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DevRegisterRequest,
      com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> getDeviceRegisterMethod() {
    io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DevRegisterRequest, com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> getDeviceRegisterMethod;
    if ((getDeviceRegisterMethod = CaCenterGrpc.getDeviceRegisterMethod) == null) {
      synchronized (CaCenterGrpc.class) {
        if ((getDeviceRegisterMethod = CaCenterGrpc.getDeviceRegisterMethod) == null) {
          CaCenterGrpc.getDeviceRegisterMethod = getDeviceRegisterMethod =
              io.grpc.MethodDescriptor.<com.ceit.desktop.grpc.caCenter.DevRegisterRequest, com.ceit.desktop.grpc.caCenter.DeviceRegisteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeviceRegister"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DevRegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DeviceRegisteReply.getDefaultInstance()))
              .setSchemaDescriptor(new CaCenterMethodDescriptorSupplier("DeviceRegister"))
              .build();
        }
      }
    }
    return getDeviceRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest,
      com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> getDeviceUnRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeviceUnRegister",
      requestType = com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest.class,
      responseType = com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest,
      com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> getDeviceUnRegisterMethod() {
    io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest, com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> getDeviceUnRegisterMethod;
    if ((getDeviceUnRegisterMethod = CaCenterGrpc.getDeviceUnRegisterMethod) == null) {
      synchronized (CaCenterGrpc.class) {
        if ((getDeviceUnRegisterMethod = CaCenterGrpc.getDeviceUnRegisterMethod) == null) {
          CaCenterGrpc.getDeviceUnRegisterMethod = getDeviceUnRegisterMethod =
              io.grpc.MethodDescriptor.<com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest, com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeviceUnRegister"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply.getDefaultInstance()))
              .setSchemaDescriptor(new CaCenterMethodDescriptorSupplier("DeviceUnRegister"))
              .build();
        }
      }
    }
    return getDeviceUnRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.SoftRegisterRequest,
      com.ceit.desktop.grpc.caCenter.SoftRegisteReply> getSoftRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SoftRegister",
      requestType = com.ceit.desktop.grpc.caCenter.SoftRegisterRequest.class,
      responseType = com.ceit.desktop.grpc.caCenter.SoftRegisteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.SoftRegisterRequest,
      com.ceit.desktop.grpc.caCenter.SoftRegisteReply> getSoftRegisterMethod() {
    io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.SoftRegisterRequest, com.ceit.desktop.grpc.caCenter.SoftRegisteReply> getSoftRegisterMethod;
    if ((getSoftRegisterMethod = CaCenterGrpc.getSoftRegisterMethod) == null) {
      synchronized (CaCenterGrpc.class) {
        if ((getSoftRegisterMethod = CaCenterGrpc.getSoftRegisterMethod) == null) {
          CaCenterGrpc.getSoftRegisterMethod = getSoftRegisterMethod =
              io.grpc.MethodDescriptor.<com.ceit.desktop.grpc.caCenter.SoftRegisterRequest, com.ceit.desktop.grpc.caCenter.SoftRegisteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SoftRegister"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.SoftRegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.SoftRegisteReply.getDefaultInstance()))
              .setSchemaDescriptor(new CaCenterMethodDescriptorSupplier("SoftRegister"))
              .build();
        }
      }
    }
    return getSoftRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceCheckRequest,
      com.ceit.desktop.grpc.caCenter.DeviceCheckReply> getDeviceCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeviceCheck",
      requestType = com.ceit.desktop.grpc.caCenter.DeviceCheckRequest.class,
      responseType = com.ceit.desktop.grpc.caCenter.DeviceCheckReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceCheckRequest,
      com.ceit.desktop.grpc.caCenter.DeviceCheckReply> getDeviceCheckMethod() {
    io.grpc.MethodDescriptor<com.ceit.desktop.grpc.caCenter.DeviceCheckRequest, com.ceit.desktop.grpc.caCenter.DeviceCheckReply> getDeviceCheckMethod;
    if ((getDeviceCheckMethod = CaCenterGrpc.getDeviceCheckMethod) == null) {
      synchronized (CaCenterGrpc.class) {
        if ((getDeviceCheckMethod = CaCenterGrpc.getDeviceCheckMethod) == null) {
          CaCenterGrpc.getDeviceCheckMethod = getDeviceCheckMethod =
              io.grpc.MethodDescriptor.<com.ceit.desktop.grpc.caCenter.DeviceCheckRequest, com.ceit.desktop.grpc.caCenter.DeviceCheckReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeviceCheck"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DeviceCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ceit.desktop.grpc.caCenter.DeviceCheckReply.getDefaultInstance()))
              .setSchemaDescriptor(new CaCenterMethodDescriptorSupplier("DeviceCheck"))
              .build();
        }
      }
    }
    return getDeviceCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CaCenterStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CaCenterStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CaCenterStub>() {
        @java.lang.Override
        public CaCenterStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CaCenterStub(channel, callOptions);
        }
      };
    return CaCenterStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CaCenterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CaCenterBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CaCenterBlockingStub>() {
        @java.lang.Override
        public CaCenterBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CaCenterBlockingStub(channel, callOptions);
        }
      };
    return CaCenterBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CaCenterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CaCenterFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CaCenterFutureStub>() {
        @java.lang.Override
        public CaCenterFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CaCenterFutureStub(channel, callOptions);
        }
      };
    return CaCenterFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CaCenterImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *终端注册
     * </pre>
     */
    public void deviceRegister(com.ceit.desktop.grpc.caCenter.DevRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDeviceRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     *终端注册注销
     * </pre>
     */
    public void deviceUnRegister(com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDeviceUnRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     *软件注册
     * </pre>
     */
    public void softRegister(com.ceit.desktop.grpc.caCenter.SoftRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.SoftRegisteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSoftRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     *硬件认证
     * </pre>
     */
    public void deviceCheck(com.ceit.desktop.grpc.caCenter.DeviceCheckRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceCheckReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDeviceCheckMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDeviceRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.ceit.desktop.grpc.caCenter.DevRegisterRequest,
                com.ceit.desktop.grpc.caCenter.DeviceRegisteReply>(
                  this, METHODID_DEVICE_REGISTER)))
          .addMethod(
            getDeviceUnRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest,
                com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply>(
                  this, METHODID_DEVICE_UN_REGISTER)))
          .addMethod(
            getSoftRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.ceit.desktop.grpc.caCenter.SoftRegisterRequest,
                com.ceit.desktop.grpc.caCenter.SoftRegisteReply>(
                  this, METHODID_SOFT_REGISTER)))
          .addMethod(
            getDeviceCheckMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.ceit.desktop.grpc.caCenter.DeviceCheckRequest,
                com.ceit.desktop.grpc.caCenter.DeviceCheckReply>(
                  this, METHODID_DEVICE_CHECK)))
          .build();
    }
  }

  /**
   */
  public static final class CaCenterStub extends io.grpc.stub.AbstractAsyncStub<CaCenterStub> {
    private CaCenterStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CaCenterStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CaCenterStub(channel, callOptions);
    }

    /**
     * <pre>
     *终端注册
     * </pre>
     */
    public void deviceRegister(com.ceit.desktop.grpc.caCenter.DevRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeviceRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *终端注册注销
     * </pre>
     */
    public void deviceUnRegister(com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeviceUnRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *软件注册
     * </pre>
     */
    public void softRegister(com.ceit.desktop.grpc.caCenter.SoftRegisterRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.SoftRegisteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSoftRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *硬件认证
     * </pre>
     */
    public void deviceCheck(com.ceit.desktop.grpc.caCenter.DeviceCheckRequest request,
        io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceCheckReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeviceCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CaCenterBlockingStub extends io.grpc.stub.AbstractBlockingStub<CaCenterBlockingStub> {
    private CaCenterBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CaCenterBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CaCenterBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *终端注册
     * </pre>
     */
    public com.ceit.desktop.grpc.caCenter.DeviceRegisteReply deviceRegister(com.ceit.desktop.grpc.caCenter.DevRegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeviceRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *终端注册注销
     * </pre>
     */
    public com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply deviceUnRegister(com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeviceUnRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *软件注册
     * </pre>
     */
    public com.ceit.desktop.grpc.caCenter.SoftRegisteReply softRegister(com.ceit.desktop.grpc.caCenter.SoftRegisterRequest request) {
      return blockingUnaryCall(
          getChannel(), getSoftRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *硬件认证
     * </pre>
     */
    public com.ceit.desktop.grpc.caCenter.DeviceCheckReply deviceCheck(com.ceit.desktop.grpc.caCenter.DeviceCheckRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeviceCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CaCenterFutureStub extends io.grpc.stub.AbstractFutureStub<CaCenterFutureStub> {
    private CaCenterFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CaCenterFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CaCenterFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *终端注册
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ceit.desktop.grpc.caCenter.DeviceRegisteReply> deviceRegister(
        com.ceit.desktop.grpc.caCenter.DevRegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeviceRegisterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *终端注册注销
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply> deviceUnRegister(
        com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeviceUnRegisterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *软件注册
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ceit.desktop.grpc.caCenter.SoftRegisteReply> softRegister(
        com.ceit.desktop.grpc.caCenter.SoftRegisterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSoftRegisterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *硬件认证
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ceit.desktop.grpc.caCenter.DeviceCheckReply> deviceCheck(
        com.ceit.desktop.grpc.caCenter.DeviceCheckRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeviceCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DEVICE_REGISTER = 0;
  private static final int METHODID_DEVICE_UN_REGISTER = 1;
  private static final int METHODID_SOFT_REGISTER = 2;
  private static final int METHODID_DEVICE_CHECK = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CaCenterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CaCenterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DEVICE_REGISTER:
          serviceImpl.deviceRegister((com.ceit.desktop.grpc.caCenter.DevRegisterRequest) request,
              (io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceRegisteReply>) responseObserver);
          break;
        case METHODID_DEVICE_UN_REGISTER:
          serviceImpl.deviceUnRegister((com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest) request,
              (io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceUnRegisteReply>) responseObserver);
          break;
        case METHODID_SOFT_REGISTER:
          serviceImpl.softRegister((com.ceit.desktop.grpc.caCenter.SoftRegisterRequest) request,
              (io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.SoftRegisteReply>) responseObserver);
          break;
        case METHODID_DEVICE_CHECK:
          serviceImpl.deviceCheck((com.ceit.desktop.grpc.caCenter.DeviceCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.ceit.desktop.grpc.caCenter.DeviceCheckReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CaCenterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CaCenterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.ceit.desktop.grpc.caCenter.CAProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CaCenter");
    }
  }

  private static final class CaCenterFileDescriptorSupplier
      extends CaCenterBaseDescriptorSupplier {
    CaCenterFileDescriptorSupplier() {}
  }

  private static final class CaCenterMethodDescriptorSupplier
      extends CaCenterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CaCenterMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CaCenterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CaCenterFileDescriptorSupplier())
              .addMethod(getDeviceRegisterMethod())
              .addMethod(getDeviceUnRegisterMethod())
              .addMethod(getSoftRegisterMethod())
              .addMethod(getDeviceCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
