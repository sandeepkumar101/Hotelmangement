package com.spi.resource;

import com.spi.user.domain.User;

/**
 */
public class ConsumerSimpleSecurityFilter extends SimpleSecurityFilter {

    @Override
    User getUser() {
        return BaseResourceTst.TEST_USER;
    }
}
