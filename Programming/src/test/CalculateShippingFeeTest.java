package test;

import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CalculateShippingFeeTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "70000, 2, 'Kim Ngưu, Hai Bà Trưng, Hà Nội', 4, 62000",
            "70000, 4, 'Kim Ngưu, Hai Bà Trưng, Hà Nội', 2, 47000",
            "70000, 0.5, 'Tam Đảo, Vĩnh Phúc', 2, 50000",
            "70000, 0.5, 'Tam Đảo, Vĩnh Phúc', 0, 30000",
            "70000, 3, 'Tam Đảo, Vĩnh Phúc', 2, 62500",
            "110000, 0.5, 'Tam Đảo, Vĩnh Phúc', 2, 0",
    })

    public void test(double price, double weight, String address, int quantityOfProductRushShipping, double expected) {
        int shippingFee = placeOrderController.calculateShippingFee(price, weight, address, quantityOfProductRushShipping);
        assertEquals(expected, shippingFee);
    }
}