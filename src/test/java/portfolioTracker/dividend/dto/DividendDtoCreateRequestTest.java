package portfolioTracker.dividend.dto;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static portfolioTracker.dividend.DividendTestUtil.*;

@SpringBootTest
class DividendDtoCreateRequestTest {
    @Autowired
    Validator validator;
/*
    @Autowired
    LocalValidatorFactoryBean validator;*/

    @Test
            void test() {
        DividendDtoCreateRequest requestDto = newAllFieldsNullCreateRequest();
        Set<ConstraintViolation<DividendDtoCreateRequest>> set = validator.validate(requestDto);
        String list = set.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        saveRequestAllFieldsNullErrorMessageList().forEach( message ->
                assertThat(list).contains(message));
    }

    @Test
            void test1() {
        DividendDtoCreateRequest victim = null;

        assertThatThrownBy(() -> validator.validate(victim))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("HV000116: The object to be validated must not be null.");
    }

}