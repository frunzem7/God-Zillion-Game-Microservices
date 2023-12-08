package student.examples.business.uservice.domain.entity;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import student.examples.business.uservice.repository.UserRepository;
import grpc.UserSignupServiceGrpc;
import grpc.UserSignupServiceOuterClass;
import grpc.UserSignupServiceOuterClass.UserSignupResponse;

@GrpcService
public class UserServiceImpl extends UserSignupServiceGrpc.UserSignupServiceImplBase {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void signup(UserSignupServiceOuterClass.UserSignupDtoRequest request,
			StreamObserver<UserSignupServiceOuterClass.UserSignupResponse> responseObserver) {
		User userEntity = new User();
		UUID id = UUID.fromString(request.getId());
		userEntity.setId(id);
		userEntity.setUsername(request.getUserName());
		userEntity.setEmail(request.getEmail());
		userEntity.setPassword(request.getPassword());

		UserSignupResponse response = UserSignupResponse.newBuilder()
				.setGreeting("Hi from business uservice, " + userEntity).build();

		responseObserver.onNext(response);

		responseObserver.onCompleted();

		userRepository.save(userEntity);
	}
}
