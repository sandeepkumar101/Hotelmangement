package com.spi.authorization;

import com.spi.authorization.impl.SessionTokenAuthorizationService;
import com.spi.user.api.ExternalUser;
import com.spi.user.exception.AuthorizationException;
import org.hamcMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @version 1.0
 * @author: 
 * 
 */
public class SessionTokenAuthorizationServiceTest extends BaseAuthorizationTst {


    @Before
    public void setUp() {
        super.setUp();
        authorizationService = new SessionTokenAuthorizationService(userRepository);
    }

    @Test
    public void authorizeUser() throws Exception {
        ExternalUser user = authorizationService.authorize(getAuthorizationRequest(AUTH_TOKEN, "user/123", null));
        assertThat(user.getId(), is(USER.getUuid().toString()));
    }

    @Test (expected = AuthorizationException.class)
    public void invalidSessionToken() {
        authorizationService.authorize(getAuthorizationRequest("abc", "abcdef", "123"));
    }

    @Test
    public void noSessionToken() {
        ExternalUser user = authorizationService.authorize(getAuthorizationRequest(null, "abcdef", null));
        assertThat(user, is(Matchers.<Object>nullValue()));
    }
}
