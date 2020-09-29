package com.spi.user.api;

import org.junit.Before;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @version 1.0
 * @author: 
 * 
 */
public class ValidationTst {

    protected Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

}
