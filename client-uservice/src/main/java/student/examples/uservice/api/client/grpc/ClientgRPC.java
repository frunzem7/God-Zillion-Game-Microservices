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
		ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8443").usePlaintext().build();

		UserSignupServiceGrpc.UserSignupServiceBlockingStub stub = UserSignupServiceGrpc.newBlockingStub(channel);

		UUID id = UUID.randomUUID();

		UserSignupServiceOuterClass.UserSignupDtoRequest request = UserSignupServiceOuterClass.UserSignupDtoRequest
				.newBuilder().setId(id.toString()).setUserName(userSignupRequest.getUsername())
				.setEmail(userSignupRequest.getEmail()).setPassword(userSignupRequest.getPassword()).build();

		log.info("Request" + request);

		UserSignupServiceOuterClass.UserSignupResponse response = stub.signup(request);

		log.info("User created: " + response);

		channel.shutdownNow();
	}

	public String deleteUserAndSend(String token) {
		ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8443").usePlaintext().build();

		UserSignupServiceGrpc.UserSignupServiceBlockingStub stub = UserSignupServiceGrpc.newBlockingStub(channel);

		UUID id = UUID.randomUUID();

		UserSignupServiceOuterClass.DeleteRequest request = UserSignupServiceOuterClass.DeleteRequest.newBuilder()
				.setToken(token).build();

		log.info("Request" + request);

		UserSignupServiceOuterClass.DeleteResponse response = stub.deleteUser(request);

		log.info("User deleted: " + response);

		channel.shutdownNow();

		return response.getUser();
	}
}
