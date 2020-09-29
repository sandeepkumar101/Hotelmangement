package com.spi.user;


import com.spi.config.ApplicationConfig;
import com.spi.user.api.AuthenticatedUserToken;
import com.spi.user.api.CreateUserRequest;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.PasswordRequest;
import com.spi.user.builder.ExternalUserBuilder;
import com.spi.user.domain.Role;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public class BaseServiceTest {

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ApplicationConfig applicationConfig;


    protected AuthenticatedUserToken createUserWithRandomUserName(Role role) {
        CreateUserRequest request = getDefaultCreateUserRequest();
        return userService.createUser(request, role);
    }

    protected CreateUserRequest getDefaultCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUser(getUser());
        request.setPassword(new PasswordRequest("password"));
        return request;
    }

    protected ExternalUser getUser() {
        ExternalUser user = ExternalUserBuilder.create().withFirstName("John")
                .withLastName("Smith")
                .withEmailAddress(createRandomEmailAddress())
                .build();
        return user;
    }

    protected String createRandomEmailAddress() {
        return RandomStringUtils.randomAlphabetic(8) + "@example.com";
    }
}
