package test;

import controller.PlaceOrderController;
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
            "khoa.ourearth.com, false",
            "khoa@.com.my, false",
            "khoa123@gmail.b, false",
            "khoa@.org.org, false",
            ".khoa@khoa.org, false",
            "khoa()*@gmail.com, false",
            "khoa..1234@yahoo.com, false",
            "khoa@ourearth.com, true",
            "khoa@you.me.net, true",
            "khoa.ownsite@ourearth.org, true",
    })
    public void test(String email, boolean expected) {
        boolean isValid = placeOrderController.validateEmail(email);
        assertEquals(expected, isValid);
    }
}