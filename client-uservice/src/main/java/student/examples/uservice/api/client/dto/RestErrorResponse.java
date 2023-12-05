package student.examples.uservice.api.client.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class RestErrorResponse extends RestResponse {

	private Map<String, String> errors;

	public RestErrorResponse(int statusCode, Map<String, String> errors) {
		super(statusCode, "failed");
		this.errors = errors;
	}
}
