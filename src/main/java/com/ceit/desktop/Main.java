package com.ceit.desktop;

import com.ceit.desktop.grpc.GrpcServerService;
import com.ceit.desktop.utils.AccessConfig;
import com.ceit.desktop.utils.JdbcUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        GrpcServerService grpcServerService = new GrpcServerService();
        try{
            grpcServerService.startGrpc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
