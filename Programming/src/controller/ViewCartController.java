package controller;

import entity.cart.Cart;

import java.sql.SQLException;

/**
 * This class controls the flow of events when users view the Cart
 *
 * @author nguyenlm
 */
public class ViewCartController extends BaseController {

    /**
     * This method checks the available products in Cart
     *
     * @throws SQLException
     */
    public void checkAvailabilityOfProduct() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }



}
