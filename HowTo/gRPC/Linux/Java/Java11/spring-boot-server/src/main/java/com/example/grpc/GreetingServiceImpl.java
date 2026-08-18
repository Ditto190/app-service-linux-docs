package com.example.grpc;

import com.example.grpc.GreetingServiceOuterClass.HelloReply;
import com.example.grpc.GreetingServiceOuterClass.HelloRequest;
import com.example.grpc.*;


import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

  // UNARY CALL - client will send a single request and server will respond with a single response
  @Override
  public void sayHello(HelloRequest request,
        StreamObserver<HelloReply> responseObserver) {

    System.out.println("UNARY client request: " + request);

    GreetingServiceOuterClass.HelloReply response = GreetingServiceOuterClass.HelloReply.newBuilder()
      .setMessage("Hello, " + request.getName())
      .build();

    responseObserver.onNext(response);

    responseObserver.onCompleted();
  };

  // SERVER STREAMING - client will send a single request and server will respond with multiple responses
  @Override
  public void sayHelloStream(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
    
    for (int i = 1; i <= 5; i++) {
        HelloReply response = HelloReply.newBuilder()
          .setMessage(request.getName() + ", response # " + i)
          .build();
        responseObserver.onNext(response);
        System.out.println("SERVER STREAM client request: " + response.getMessage());
    }
    responseObserver.onCompleted();
  }


  // CLIENT STREAMING - client will send multiple requests and server will respond with a single response
  @Override
  public StreamObserver<HelloRequest> sayHelloStreamClient(final StreamObserver<HelloReply> responseObserver){
    return new StreamObserver<HelloRequest>() {
      int count;

      @Override
      public void onNext(HelloRequest request) {
        count++;
        System.out.println("CLIENT STREAM client request: " + request.getName());
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(HelloReply.newBuilder()
        .setMessage("Client received " + count + " messages")
        .build());
        responseObserver.onCompleted();
      }

    };
  }

}
