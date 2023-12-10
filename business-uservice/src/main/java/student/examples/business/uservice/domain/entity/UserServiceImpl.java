package student.examples.business.uservice.domain.entity;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import student.examples.business.uservice.email.EmailService;
import student.examples.business.uservice.repository.UserRepository;
import student.examples.business.uservice.utils.EncryptDecryptUtils;
import grpc.UserSignupServiceGrpc;
import grpc.UserSignupServiceOuterClass;
import grpc.UserSignupServiceOuterClass.DeleteRequest;
import grpc.UserSignupServiceOuterClass.DeleteResponse;
import grpc.UserSignupServiceOuterClass.UserSignupResponse;

@Slf4j
@GrpcService
public class UserServiceImpl extends UserSignupServiceGrpc.UserSignupServiceImplBase {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;

	@Override
	public void signup(UserSignupServiceOuterClass.UserSignupDtoRequest request,
			StreamObserver<UserSignupServiceOuterClass.UserSignupResponse> responseObserver) {

		User userEntity = new User();
		UUID id = UUID.fromString(request.getId());
		userEntity.setId(id);
		userEntity.setUsername(request.getUserName());
		userEntity.setEmail(request.getEmail());
		userEntity.setPassword(EncryptDecryptUtils.encrypt(request.getPassword()));
		userEntity.setToken(EncryptDecryptUtils.encodeToBase64(request.getId() + request.getEmail()));
		userEntity.setIsActive(false);

		UserSignupResponse response = UserSignupResponse.newBuilder()
				.setGreeting("Hi from business uservice, " + userEntity).build();

		responseObserver.onNext(response);

		responseObserver.onCompleted();

		User savedUser = userRepository.save(userEntity);

		if (savedUser != null) {
			emailService.sendEmail(userRepository.getDistinctByEmail(userEntity.getEmail()));
			log.info("Email was sent successfully!");
		}
	}

	@Override
	public void deleteUser(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
		System.out.println(request.getToken());

		emailService.sendDeleteEmail(userRepository.getUserByToken(request.getToken()).get());

		DeleteResponse deleteResponse = DeleteResponse.newBuilder().setUser("Email with delete confirmation send")
				.build();

		responseObserver.onNext(deleteResponse);

		responseObserver.onCompleted();
	}

}
