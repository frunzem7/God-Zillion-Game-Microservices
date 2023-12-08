package student.examples.uservice.api.client.grpc;

import java.util.UUID;

import grpc.UserSignupServiceGrpc;
import grpc.UserSignupServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import student.examples.uservice.api.client.dto.UserSignupRequest;

@Slf4j
public class ClientgRPC {
	public void createUserAndSend(UserSignupRequest userSignupRequest) {
		log.info("I'm here, on the client side");

		ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8443").usePlaintext().build();

		UserSignupServiceGrpc.UserSignupServiceBlockingStub stub = UserSignupServiceGrpc.newBlockingStub(channel);

		UUID id = UUID.randomUUID();

		UserSignupServiceOuterClass.UserSignupDtoRequest request = UserSignupServiceOuterClass.UserSignupDtoRequest
				.newBuilder().setId(id.toString()).setUserName(userSignupRequest.getUsername())
				.setEmail(userSignupRequest.getEmail()).setPassword(userSignupRequest.getPassword()).build();

		log.info("request" + request);

		UserSignupServiceOuterClass.UserSignupResponse response = stub.signup(request);

		log.info("User created: " + response);

		channel.shutdownNow();
	}
}
