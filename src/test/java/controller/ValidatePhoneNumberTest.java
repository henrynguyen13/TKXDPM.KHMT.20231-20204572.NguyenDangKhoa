package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePhoneNumberTest {
    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }



    @ParameterizedTest
    @CsvSource({
            "0978014692, true",
            "1345582394, false",
            "097801469, false",
            "04859der2, false",
            " , false",
    })
    public void test(String name, boolean expected) {
        boolean isValid = placeOrderController.validatePhoneNumber(name);
        assertEquals(expected, isValid);
    }


}