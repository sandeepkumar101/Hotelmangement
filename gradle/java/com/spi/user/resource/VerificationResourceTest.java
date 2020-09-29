package com.spi.user.resource;

import com.spi.resource.BaseResourceTst;
import com.spi.resource.ConsumerSimpleSecurityFilter;
import com.spi.user.api.EmailVerificationRequest;
import com.spi.user.domain.User;
import com.spi.user.domain.VerificationToken;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;

import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 *
 * @version 1.0
 * @author:  
 * 
 */
public class VerificationResourceTest extends BaseResourceTst {

    public VerificationResourceTest() {
        super(new WebAppDescriptor.Builder()
                .contextPath("spring")
                .contextParam("contextConfigLocation", "classpath:integration-test-context.xml")
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .contextParam("spring.profiles.active", "dev")
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .initParam(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, ConsumerSimpleSecurityFilter.class.getName())
                .build());
    }

    @Test
    public void verify() {
        VerificationToken token = new VerificationToken(new User(), VerificationToken.VerificationTokenType.emailVerification, 120);
        when(verificationTokenService.verify("123")).thenReturn(token);
        ClientResponse response = super.resource().path("verify/tokens/" + UUID.randomUUID())
                .accept(APPLICATION_JSON).post(ClientResponse.class);
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void generateEmailToken() {
        VerificationToken token = new VerificationToken(new User(), VerificationToken.VerificationTokenType.emailVerification, 120);
        when(verificationTokenService.generateEmailVerificationToken("test@example.com")).thenReturn(token);
        ClientResponse response = super.resource().path("verify/tokens").entity(new EmailVerificationRequest("test@example.com"), APPLICATION_JSON)
                .accept(APPLICATION_JSON).post(ClientResponse.class);
        assertThat(response.getStatus(), is(200));
    }

}
