package authservertester;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.workday.webclient.*;

public class AuthServiceTests {
	static HttpClient client = new HttpClient();

	@Test
	public void grantToken() throws InterruptedException {
		client.addBodyParameter("request-originator", "UI");
		client.addBodyParameter("username", "superuser");
		client.addBodyParameter("password", "Da7@+%mfbMErS7at");
		client.addBodyParameter("remote-ip-address", "127.0.0.1");

		JsonResponse resp = client.post("http://localhost:12766/auth-server/services/super/api/v1/token");
		assertEquals(resp.getStatus().toString(), "200");
		System.out.println(resp.getJsonString());
		new Stopwatch(5);
		System.out.println(resp.getJsonString());
	}
}
