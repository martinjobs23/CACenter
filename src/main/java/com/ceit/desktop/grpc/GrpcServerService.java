package com.ceit.desktop.grpc;

import com.ceit.desktop.grpc.caCenter.*;
import com.ceit.desktop.service.CertRequestService;
import com.ceit.desktop.utils.FileConfigUtil;
import com.ceit.desktop.utils.JdbcUtil;
import com.ceit.desktop.utils.Result;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GrpcServerService extends CaCenterGrpc.CaCenterImplBase {

    private static Logger logger = LoggerFactory.getLogger(GrpcServerService.class);

    JdbcUtil jdbcUtil = new JdbcUtil();

    CertRequestService certRequestService = new CertRequestService();

    public GrpcServerService() throws IOException {
    }

    public void startGrpc() throws IOException, InterruptedException {
        FileConfigUtil fileConfigUtil = new FileConfigUtil();
        String port = fileConfigUtil.load("desktop.properties","ca.grpc.port");
        logger.info("CaCenter start gRPC listening on " + port);
        ServerBuilder.forPort(Integer.parseInt(port))
                .addService(new GrpcServerService())
                .build()
                .start()
                .awaitTermination();
    }

    //终端注册接口
    public void deviceRegister(DevRegisterRequest request, StreamObserver<DeviceRegisteReply> replyStreamObserver) {
//        try {
            Result result = certRequestService.dev_Cert_Req(request);
            replyStreamObserver.onNext(DeviceRegisteReply.newBuilder().setCode(result.getCode()).setMsg(result.getMsg()).setData(result.getData().toString()).build());
            replyStreamObserver.onCompleted();
//        } catch (IOException e) {
//        e.printStackTrace();
//    }
        //Result result1 = certRequestService.dec_Cert_Req_selfCA(request);

    }

    //终端注册撤销接口
    public void deviceUnRegister(DeviceUnRegisterRequest request, StreamObserver<DeviceUnRegisteReply> replyStreamObserver){
        Result result = certRequestService.dev_Cert_Revoke(request);
        replyStreamObserver.onNext(DeviceUnRegisteReply.newBuilder().setStatus(result.getCode()).setMsg(result.getMsg()).setResult((String) result.getData()).build());
        replyStreamObserver.onCompleted();
    }

    //硬件认证接口
    public void deviceCheck(DeviceCheckRequest request, StreamObserver<DeviceCheckReply> replyStreamObserver){
        String device_hash= request.getDeviceHash();
        String device_ip = request.getDeviceIp();
        System.out.println("获取到device hash: " + device_hash);
        Result result = certRequestService.dev_Check(device_hash,device_ip);
        System.out.println("result: " + result.toString());
        replyStreamObserver.onNext(DeviceCheckReply.newBuilder().setMsg(result.getMsg()).setResult((String) result.getData()).setStatus(result.getCode()).build());
        replyStreamObserver.onCompleted();
    }

    public void softRegister(SoftRegisterRequest request, StreamObserver<SoftRegisteReply> replyStreamObserver) {
//        try {
            Result result = certRequestService.soft_Register(request);
            System.out.println("result: " + result.toString());
            replyStreamObserver.onNext(SoftRegisteReply.newBuilder().setMsg(result.getMsg()).setResult((String) result.getData()).setStatus(result.getCode()).build());
            replyStreamObserver.onCompleted();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
