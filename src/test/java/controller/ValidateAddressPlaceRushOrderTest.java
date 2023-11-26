package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidateAddressPlaceRushOrderTest {

    private PlaceOrderController placeOrderController;

    /**
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "'Kim Ngưu, Hai Bà Trưng, Hà Nội', true",
            "'Tam Đảo, Vĩnh Phúc', false",
            " , false",
    })

    public void test(String address, boolean expected) {
        boolean isValid = placeOrderController.validateAddressPlaceRushOrder(address);
        assertEquals(expected, isValid);
    }
}