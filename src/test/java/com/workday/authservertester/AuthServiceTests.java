package com.workday.authservertester;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.workday.webclient.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class AuthServiceTests {
    HttpClient client;
    int SECONDS_PER_MINUTE = 60;
    String OLD_TOKEN = "eyJhbGciOiJSUzUxMiIsImtpZCI6IkFTX25vZGUxXzA4MjQyMDE1MDg0NjQ4NTAwIn0.eyJpc3MiOiJPTVMiLCJzdWIiOiJTdXBlclVzZXIiLCJhdWQiOiJ3ZCIsImV4cCI6MTQ0MDQ1NDIyOSwiaWF0IjoxNDQwNDU0MDQ5LCJhdXRoX3RpbWUiOjE0NDA0NTQwNDkxMjAsImp0aSI6Im5seXpieWJvdXJhMTVrZHRkOHZnbTk2Nzk5YnlkMDd4aXJ3cXg5b25iZWg2MWhqY2t1N3JiM3Bpc3JvNzBmcjlwYWwyOGxpZ2xvNmJ2b29meGt3MWlzMGtrODVtdGgzN2J3NnRhMHJibGl2ZWZ5OXV1YjZiZjBrY2JydDA3bmNwc2FvaXN3Z20yODRvdWlucWkwYWpmd24xeDIwbHVxcjRxeHdjOWQwYmF6M3VxZGx3MHljcTcyZHM2NG93NXpxYmU3NDQyZmliNWViMDFvMzlrMmdvcXN6c2RhbGhtNTF4NDZtYXk2MTdwcGIxMmkyaXg5M3NtZjJsbnBseHhtbXJxZnpvdXM1ZzYwY2oycnlrMnBzcHJjbHdlZ3JhYzB5ajZkemw2eWo1ZnMyeDN4MTI4cDJ0NW9mMnE2bXNxZjNrNjV0YzV1ZmhkeWp0czhkdGt3cGJkZXZ5aXA3NGV6Mmx3bXJ5ZmdiZmU4MzM1bnBncWQzc3N6NmpzdDlkMnBvcnhwcDk0N2d5bHhpMiIsImNoYW5uZWwiOiJVSSIsImF1dGhfdHlwZSI6IlVuaXZlcnNhbFBhc3N3b3JkIiwic3lzX2FjY3RfdHlwIjoiTiIsInRlbmFudCI6InN1cGVyIiwic2NvcGUiOnt9LCJ0b2tlblR5cGUiOiJJZGVudGl0eSJ9.bYr0uX1xu-XER6JSvfneb33twiodSgAjckDAMkAfttNqm8mPtkO1SsE4FJx13iP8wQChyF43TVdGoRf8tMB28t6M28CRluoJWEhaOWlAqRTXimGem0fIa6EvaAIj2D4uQATHHH_qCFYomrhy0ottfYmqp5Jpevuw8yzAkohH_yWeNaSKFu9ujf6IennmM26piR7UwSqqKKxoHsxvhfTaK9OvYyrUVsxQd9I0UIBNO724t170XDi5Q_I9LUMwdiYo";
    static Logger log = Logger.getLogger(AuthServiceTests.class.getName());

    @Before
    public void testSetup() {
        client = new HttpClient();
    }

    @Test
    public void grantToken() throws InterruptedException {
        JsonResponse resp = requestToken();
        log.debug("token: " + resp.getJsonString());
        assertEquals(resp.getStatus().toString(), "200");
        assertTrue(isValidToken(resp.getJsonString()));
    }

    @Test
    public void tokenShouldBeValidWithinThreeMin() throws InterruptedException, JsonProcessingException, IOException {
        JsonResponse resp = requestToken();
        String token = resp.getJsonString();
        log.debug("Token: " + token);

        client.addHeader("Authorization", "ID " + token);
        JsonResponse verifyResp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token/verify");
        boolean tokenIsValid = Boolean.parseBoolean(verifyResp.returnKeyValue("valid"));
        long tokenExpiresAt = Long.parseLong(verifyResp.returnKeyValue("expirationTime"));
        log.info("tokenIsValid: " + tokenIsValid);
        log.debug(tokenExpiresAt + " : tokenExpiration");
        log.debug(getTimestamp() + " : currentTimeInSeconds");
        assertTrue(tokenIsValid);
    }

    @Ignore @Test
    public void tokenShouldBeInvalidAfterThreeMin() throws JsonProcessingException, IOException {
        JsonResponse resp = requestToken();
        String token = resp.getJsonString();
        log.debug("Token: " + token);

        client.addHeader("Authorization", "ID " + token);
        new Stopwatch(4 * SECONDS_PER_MINUTE);
        JsonResponse verifyResp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token/verify");
        boolean tokenIsValid = Boolean.parseBoolean(verifyResp.returnKeyValue("valid"));
        long tokenExpiresAt = Long.parseLong(verifyResp.returnKeyValue("expirationTime"));
        log.info("tokenIsValid: " + tokenIsValid);
        log.debug(tokenExpiresAt + " : tokenExpiration");
        log.debug(getTimestamp() + " : currentTimeInSeconds");
        assertFalse(tokenIsValid);
    }

    @Ignore @Test
    public void tokenExpirationTimer() throws JsonProcessingException, IOException {
        JsonResponse resp = requestToken();
        String token = resp.getJsonString();
        boolean tokenIsValid = true;
        long tokenExpiresAt = 0;
        client.addHeader("Authorization", "ID " + token);

        JsonResponse verifyResp;
        while (tokenIsValid) {
            verifyResp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token/verify");
            tokenIsValid = Boolean.parseBoolean(verifyResp.returnKeyValue("valid"));
            tokenExpiresAt = Long.parseLong(verifyResp.returnKeyValue("expirationTime"));

            log.info("tokenIsValid: " + tokenIsValid);
            log.debug(tokenExpiresAt + " : tokenExpiration");
            log.debug(getTimestamp() + " : currentTimeInSeconds");
            new Stopwatch(1 * SECONDS_PER_MINUTE);
        }
        assertTrue(getTimestamp() - tokenExpiresAt <= 180);
    }

    @Test
    public void tokenEndpointShouldReturnValidInfo() throws IOException {
        client.addHeader("Authorization", "ID " + OLD_TOKEN);
        JsonResponse resp = client.get("http://localhost:12766/auth-server/services/super/api/v1/token");
        log.debug(resp.getJsonString());
        String[] keys;
        keys = new String[] {"header", "alg"};
        assertEquals("RS512", resp.returnKeyValue(keys));

        keys = new String[] {"header", "kid"};
        assertEquals("AS_node1_08242015084648500", resp.returnKeyValue(keys));

        keys = new String[] {"body", "iss"};
        assertEquals("OMS", resp.returnKeyValue(keys));

        keys = new String[] {"body", "sub"};
        assertEquals("SuperUser", resp.returnKeyValue(keys));

        keys = new String[] {"body", "aud"};
        assertEquals("wd", resp.returnKeyValue(keys));

        keys = new String[] {"body", "exp"};
        assertEquals(1440454229, Long.parseLong(resp.returnKeyValue(keys)));

        keys = new String[] {"body", "iat"};
        assertEquals(1440454049, Long.parseLong(resp.returnKeyValue(keys)));

        keys = new String[] {"body", "auth_time"};
        assertEquals(new BigInteger("1440454049120"), new BigInteger(resp.returnKeyValue(keys)));

        keys = new String[] {"body", "jti"};
        assertTrue(isValidToken(resp.returnKeyValue(keys)));

        keys = new String[] {"body", "channel"};
        assertEquals("UI", resp.returnKeyValue(keys));

        keys = new String[] {"body", "auth_type"};
        assertEquals("UniversalPassword", resp.returnKeyValue(keys));

        keys = new String[] {"body", "sys_acct_typ"};
        assertEquals("N", resp.returnKeyValue(keys));

        keys = new String[] {"body", "tenant"};
        assertEquals("super", resp.returnKeyValue(keys));

        keys = new String[] {"body", "tokenType"};
        assertEquals("Identity", resp.returnKeyValue(keys));

        keys = new String[] {"signature"};
        assertTrue(isValidToken(resp.returnKeyValue(keys)));
    }

    /**
     * Posts to the token endpoint to request a new token for a user
     * @return
     */
    private JsonResponse requestToken() {
        // TODO: Parametertize this for username and password
        client.addBodyParameter("request-originator", "UI");
        client.addBodyParameter("username", "superuser");
        client.addBodyParameter("password", "Da7@+%mfbMErS7at");
        client.addBodyParameter("remote-ip-address", "127.0.0.1");

        return client.post("http://localhost:12766/auth-server/services/super/api/v1/token");
    }

    /**
     * Returns current epoch timestamp in seconds
     * @return
     */
    private long getTimestamp() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    /**
     * Checks if token is a string that consists of (a-zA-Z0-9_-.)
     * @param token
     * @return
     */
    private boolean isValidToken(String token) {
        return (token.matches("^(\\w|-|\\.)*$"));
    }
}
