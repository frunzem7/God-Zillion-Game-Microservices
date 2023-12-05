package student.examples.uservice.api.client.dto;

import lombok.Getter;

@Getter
public class RestSuccessResponse extends RestResponse {

	private Object body;

	public RestSuccessResponse(int statusCode, Object body) {
		super(statusCode, "success");
		this.body = body;
	}
}
