package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidateEmailTest {
    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "mysite.ourearth.com, false",
            "mysite@.com.my, false",
            "mysite123@gmail.b, false",
            "mysite@.org.org, false",
            ".mysite@mysite.org, false",
            "mysite()*@gmail.com, false",
            "mysite..1234@yahoo.com, false",
            "mysite@ourearth.com, true",
            "mysite@you.me.net, true",
            "my.ownsite@ourearth.org, true",
    })
    public void test(String email, boolean expected) {
        boolean isValid = placeOrderController.validateEmail(email);
        assertEquals(expected, isValid);
    }
}