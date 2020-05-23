package com.cgm.qanda.util;

import com.cgm.qanda.QnAApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = QnAApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class TestValidationUtil {
    @Test
    public void testValidateCorrectLength() {
        String input = "test String";
        boolean validate = ValidationUtil.validateLength(input);
        assertEquals(true, validate);
    }

    @Test
    public void testValidateLengthFailedWithNullValue() {
        String input = null;
        boolean validate = ValidationUtil.validateLength(input);
        assertEquals(false, validate);
    }

    @Test
    public void testValidateIncorrectAnswerFormatWithSingleQuotation() {
        String input = "this is input " + "\"" + "test";
        boolean validate = ValidationUtil.validateAnswerFormat(input);
        assertEquals(false, validate);
    }

    @Test
    public  void testValidateIncorrectAnswerFormatWithoutQuotation() {
        String input1 = "this is wrong input";
        boolean validate1 = ValidationUtil.validateAnswerFormat(input1);
        assertEquals(false, validate1);
    }

    @Test
    public void testValidateLengthWithIncorrectValue() {
        String input =  "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a " +
                "piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, " +
                "a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin " +
                "words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in " +
                "classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 " +
                "and 1.10.33 of de Finibus Bonorum et Malorum (The Extremes of Good and Evil) by Cicero, " +
                "written in 45 BC. This book is a treatise on the theory of ethics, very popular during the " +
                "Renaissance Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a " +
                "piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock";
        boolean validate = ValidationUtil.validateLength(input);
        assertEquals(false, validate);
    }

}
