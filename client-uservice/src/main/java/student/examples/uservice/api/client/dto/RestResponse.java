package student.examples.uservice.api.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestResponse {
	private int statusCode;
	private String statusMessage;

}
