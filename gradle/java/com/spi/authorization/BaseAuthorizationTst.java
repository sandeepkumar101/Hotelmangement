package com.spi.authorization;

import com.spi.authorization.impl.SessionTokenAuthorizationService;
import com.spi.config.ApplicationConfig;
import com.spi.user.UserRepository;
import com.spi.user.domain.AuthorizationToken;
import com.spi.user.domain.User;
import com.spi.util.DateUtil;
import org.junit.Before;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @version 1.0
 * @author: 
 * 
 */
public class BaseAuthorizationTst {

    protected static String AUTH_TOKEN;
    protected static final User USER = new User();
    {
        USER.setAuthorizationToken(new AuthorizationToken(USER));
        AUTH_TOKEN = USER.getAuthorizationToken().getToken();
    }

    protected AuthorizationService authorizationService;
    protected UserRepository userRepository;
    protected ApplicationConfig applicationConfig;

    @Before
    public void setUp() {
         userRepository = mock(UserRepository.class);
         when(userRepository.findByUuid(eq(USER.getUuid().toString()))).thenReturn(USER);
         when(userRepository.findBySession(eq(AUTH_TOKEN))).thenReturn(USER);
         applicationConfig = mock(ApplicationConfig.class);
         when(applicationConfig.getSessionDateOffsetInMinutes()).thenReturn(30);
         authorizationService = new SessionTokenAuthorizationService(userRepository);
    }

    protected AuthorizationRequestContext getAuthorizationRequest(String hashedToken, String requestString, String nonce) {
        return getAuthorizationRequest(hashedToken, requestString, DateUtil.getCurrentDateAsIso8061String(), nonce);
    }

    protected AuthorizationRequestContext getAuthorizationRequest(String hashedToken, String requestString, String dateString, String nonce) {
        AuthorizationRequestContext authRequest = new AuthorizationRequestContext(requestString, "POST", dateString, nonce, hashedToken);
        return authRequest;
    }
}
