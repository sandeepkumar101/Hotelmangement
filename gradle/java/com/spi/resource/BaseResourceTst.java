package com.spi.resource;

import com.spi.user.UserService;
import com.spi.user.VerificationTokenService;
import com.spi.user.api.LoginRequest;
import com.spi.user.api.PasswordRequest;
import com.spi.user.builder.ExternalUserBuilder;
import com.spi.gateway.EmailServicesGateway;
import com.spi.mock.AppMockConfiguration;
import com.spi.user.domain.Role;
import com.spi.user.domain.AuthorizationToken;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.UpdateUserRequest;
import com.spi.user.domain.User;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.test.context.ActiveProfiles;

/**
 */
@ActiveProfiles(profiles = "dev")
public class BaseResourceTst extends JerseyTest {

    public BaseResourceTst(WebAppDescriptor descriptor) {
        super(descriptor);
    }

    protected static User TEST_USER;
    protected static String FIRST_NAME = "Test";
    protected static String LAST_NAME = "User";
    protected static String EMAIL_ADDRESS = "test@example.com";
    protected static String USERNAME = "testuser";
    protected static String PASSWORD = "password";
    protected static PasswordRequest PASSWORD_REQUEST;

    {
        TEST_USER = new User();
        TEST_USER.setFirstName(FIRST_NAME);
        TEST_USER.setLastName(LAST_NAME);
        TEST_USER.setEmailAddress(EMAIL_ADDRESS);
        TEST_USER.setRole(Role.authenticated);
    }

    {
        PASSWORD_REQUEST = new PasswordRequest();
        PASSWORD_REQUEST.setPassword(PASSWORD);
    }

    protected static AuthorizationToken AUTH_TOKEN;
    {
        TEST_USER.setAuthorizationToken(new AuthorizationToken(TEST_USER));
        AUTH_TOKEN = TEST_USER.getAuthorizationToken();
    }

    protected static ApplicationContext appCtx;


    public static class ApplicationContextAccess implements ApplicationContextAware {
        public void setApplicationContext(ApplicationContext ctx) {
            appCtx = ctx;
        }
    }

    protected UserService userService;
    protected VerificationTokenService verificationTokenService;
    protected ConnectionFactoryLocator connectionFactoryLocator;
    protected EmailServicesGateway emailServicesGateway;


    protected Environment environment;

    /**
     * Relies on component scanning of mock services from {@link com.spi.mock.AppMockConfiguration}
     */
    @Before
    public void setUpMocks() {
        AppMockConfiguration config = appCtx.getBean(AppMockConfiguration.class);
        userService = config.userService();
        verificationTokenService = config.verificationTokenService();
        connectionFactoryLocator = (ConnectionFactoryLocator) appCtx.getBean("connectionFactoryLocator");
        environment = config.environment();
        emailServicesGateway = config.emailServicesGateway();
    }

    protected CreateUserRequest createSignupRequest() {
        return new CreateUserRequest(ExternalUserBuilder.create().withEmailAddress(TEST_USER.getEmailAddress())
                .withFirstName(TEST_USER.getFirstName()).withLastName(TEST_USER.getLastName()).build(), PASSWORD_REQUEST);
    }


    protected LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername(TEST_USER.getEmailAddress());
        request.setPassword(PASSWORD);
        return request;
    }

    protected UpdateUserRequest createUpdateUserRequest(String emailAddress) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmailAddress(emailAddress);
        return request;
    }

}
