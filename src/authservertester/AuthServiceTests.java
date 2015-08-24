package authservertester;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.workday.webclient.*;
import java.io.IOException;
import org.apache.log4j.Logger;

public class AuthServiceTests {
	static HttpClient client = new HttpClient();
	int SECONDS_PER_MINUTE = 60;
	String OLD_TOKEN = "eyJhbGciOiJSUzUxMiIsImtpZCI6IkFTX25vZGUxXzA4MjQyMDE1MDg0NjQ4NTAwIn0.eyJpc3MiOiJPTVMiLCJzdWIiOiJTdXBlclVzZXIiLCJhdWQiOiJ3ZCIsImV4cCI6MTQ0MDQ1NDIyOSwiaWF0IjoxNDQwNDU0MDQ5LCJhdXRoX3RpbWUiOjE0NDA0NTQwNDkxMjAsImp0aSI6Im5seXpieWJvdXJhMTVrZHRkOHZnbTk2Nzk5YnlkMDd4aXJ3cXg5b25iZWg2MWhqY2t1N3JiM3Bpc3JvNzBmcjlwYWwyOGxpZ2xvNmJ2b29meGt3MWlzMGtrODVtdGgzN2J3NnRhMHJibGl2ZWZ5OXV1YjZiZjBrY2JydDA3bmNwc2FvaXN3Z20yODRvdWlucWkwYWpmd24xeDIwbHVxcjRxeHdjOWQwYmF6M3VxZGx3MHljcTcyZHM2NG93NXpxYmU3NDQyZmliNWViMDFvMzlrMmdvcXN6c2RhbGhtNTF4NDZtYXk2MTdwcGIxMmkyaXg5M3NtZjJsbnBseHhtbXJxZnpvdXM1ZzYwY2oycnlrMnBzcHJjbHdlZ3JhYzB5ajZkemw2eWo1ZnMyeDN4MTI4cDJ0NW9mMnE2bXNxZjNrNjV0YzV1ZmhkeWp0czhkdGt3cGJkZXZ5aXA3NGV6Mmx3bXJ5ZmdiZmU4MzM1bnBncWQzc3N6NmpzdDlkMnBvcnhwcDk0N2d5bHhpMiIsImNoYW5uZWwiOiJVSSIsImF1dGhfdHlwZSI6IlVuaXZlcnNhbFBhc3N3b3JkIiwic3lzX2FjY3RfdHlwIjoiTiIsInRlbmFudCI6InN1cGVyIiwic2NvcGUiOnt9LCJ0b2tlblR5cGUiOiJJZGVudGl0eSJ9.bYr0uX1xu-XER6JSvfneb33twiodSgAjckDAMkAfttNqm8mPtkO1SsE4FJx13iP8wQChyF43TVdGoRf8tMB28t6M28CRluoJWEhaOWlAqRTXimGem0fIa6EvaAIj2D4uQATHHH_qCFYomrhy0ottfYmqp5Jpevuw8yzAkohH_yWeNaSKFu9ujf6IennmM26piR7UwSqqKKxoHsxvhfTaK9OvYyrUVsxQd9I0UIBNO724t170XDi5Q_I9LUMwdiYo";
	static Logger log = Logger.getLogger(AuthServiceTests.class.getName());

	@Test
	public void grantToken() throws InterruptedException {
		JsonResponse resp = requestToken();
		assertEquals(resp.getStatus().toString(), "200");
		System.out.println(resp.getJsonString());
		assertTrue(resp.getJsonString().matches("^(\\w|-|\\.)*$")); // matches a-z A-Z 0-9 _ - .
	}

	@Test
	public void tokenShouldBeValidWithinThreeMin() throws InterruptedException, JsonProcessingException, IOException {
		JsonResponse resp = requestToken();
		String token = resp.getJsonString();
		log.debug("Token: " + token);

		client.addHeader("Authorization", "ID " + token);
		JsonResponse verifyResp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token/verify");
		boolean tokenIsValid = Boolean.parseBoolean(verifyResp.returnKeyValue("valid"));
		log.info("tokenIsValid: " + tokenIsValid);
		assertTrue(tokenIsValid);
	}

	@Ignore("Slow test") @Test
	public void tokenShouldBeInvalidAfterThreeMin() throws JsonProcessingException, IOException {
		JsonResponse resp = requestToken();
		String token = resp.getJsonString();
		log.debug("Token: " + token);

		client.addHeader("Authorization", "ID " + token);
		new Stopwatch(4 * SECONDS_PER_MINUTE);
		JsonResponse verifyResp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token/verify");
		boolean tokenIsValid = Boolean.parseBoolean(verifyResp.returnKeyValue("valid"));
		log.info("tokenIsValid: " + tokenIsValid);
		assertFalse(tokenIsValid);
	}

	@Test
	public void tokenEndpointShouldReturnInfo() {
		client.addHeader("Authorization", "ID " + OLD_TOKEN);
		JsonResponse resp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token");
		System.out.println(resp.getJsonString());
	}

	private JsonResponse requestToken() {
		client.addBodyParameter("request-originator", "UI");
		client.addBodyParameter("username", "superuser");
		client.addBodyParameter("password", "Da7@+%mfbMErS7at");
		client.addBodyParameter("remote-ip-address", "127.0.0.1");

		return client.post("http://localhost:12766/auth-server/services/super/api/v1/token");
	}
}
