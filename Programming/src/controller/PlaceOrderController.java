package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class controls the flow of place order usecase in our AIMS project
 *
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder
     * button
     *
     * @throws SQLException
     */
    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     *
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     *
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {

    }


    /**
     * @param phoneNumber
     * @return boolean
     */
    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        //if regex doesn't have a series of consecutive digits
        if (!phoneNumber.matches("\\d+")) {
            return false;
        }
        if (phoneNumber.length() != 10) {
            return false;
        }
        return phoneNumber.charAt(0) == '0';
    }


    /**
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        if(name == null || name.isEmpty()) {
            return false;
        }

        return name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
    }


    /*
     * @param email
     * @return boolean
     */
    public boolean validateEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.matches(emailRegex, email);
    }


    /**
     * @param address
     * @return boolean
     */
    public boolean validateAddress(String address) {
        // Check address is not null
        if (address == null)
            return false;
        // Check if contain leter space only
        if (address.trim().length() == 0)
            return false;
        // Check if contain only leter and space
        if (address.matches("^[a-zA-Z ]*$") == false)
            return false;
        return true;
    }

    /**
     * This method calculates the shipping fees of order
     *
     * @param order
     * @return shippingFee
     */
//    public int calculateShippingFee(Order order) {
//        Random rand = new Random();
//        int fees = (int) (((rand.nextFloat() * 10) / 100) * order.getAmount());
//        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
//        return fees;
//    }

//    public int calculateShippingFee(double price, double weight, String address, int quantityOfProductRushShipping){
//
//        if (price > 100) {
//            return 0;
//        }
//
//        double baseCost = 0;
//        double baseWeight = 0;
//        double additionalCostPerHalfKg = 0;
//        if (address.toLowerCase().contains("hà nội") || address.toLowerCase().contains("hồ chí minh")) {
//            baseCost = 22;
//            baseWeight = 3;
//            additionalCostPerHalfKg = 2.5;
//        } else {
//            baseCost = 30;
//            baseWeight = 0.5;
//            additionalCostPerHalfKg = 2.5;
//        }
//
//        double rushShippingCost = 10 * quantityOfProductRushShipping;
//        double regularShippingCost = 0;
//
//        if(weight <= baseWeight){
//            regularShippingCost = baseCost;
//        }
//        else{
//            regularShippingCost = baseCost + Math.ceil((weight - baseWeight) * 2) * additionalCostPerHalfKg;
//        }
//
//        return (int) (rushShippingCost + regularShippingCost);
//    }
    public int calculateShippingFee(double price, double weight, String address, Order order){

        if (price > 100) {
            return 0;
        }

        double baseCost = 0;
        double baseWeight = 0;
        double additionalCostPerHalfKg = 0;
        if (address.toLowerCase().contains("hà nội") || address.toLowerCase().contains("hồ chí minh")) {
            baseCost = 22;
            baseWeight = 3;
            additionalCostPerHalfKg = 2.5;
        } else {
            baseCost = 30;
            baseWeight = 0.5;
            additionalCostPerHalfKg = 2.5;
        }
        double rushShippingCost = 0;

        if (order != null) {
            for (Object object : order.getlstOrderMedia()) {
                OrderMedia orderMedia = (OrderMedia) object;
                Media media = orderMedia.getMedia();


                if (media.getIsSupportedPlaceRushOrder()) {
                    int quantityOfProductRushShipping = orderMedia.getQuantity();
                     rushShippingCost = 10 * quantityOfProductRushShipping;

                }

            }
        }
        double regularShippingCost = 0;

        if(weight <= baseWeight){
            regularShippingCost = baseCost;
        }
        else{
            regularShippingCost = baseCost + Math.ceil((weight - baseWeight) * 2) * additionalCostPerHalfKg;
        }

        return (int) (rushShippingCost + regularShippingCost);
    }

    /**
     * This method get product available place rush order media
     *
     * @param order
     * @return media
     * @throws SQLException
     */
    public Media getProductAvailablePlaceRush(Order order) throws SQLException {
        Media media = new Media();
        HashMap<String, String> deliveryInfo = order.getDeliveryInfo();
        validateAddressPlaceRushOrder( deliveryInfo.get("address"));
        for (Object object : order.getlstOrderMedia()) {
            // CartMedia cartMedia = (CartMedia) object;
            validateMediaPlaceRushorder();
        }
        return media;
    }


    /**
     * @param address
     * @return boolean
     */
    public boolean validateAddressPlaceRushOrder( String address) {
        if(address == null || address.isEmpty()) return false;
        String lowercaseAddress = address.toLowerCase();
        return lowercaseAddress.contains("hà nội");
    }


    /**
     * @return boolean
     */
    public boolean validateMediaPlaceRushorder() {
        if (Media.getIsSupportedPlaceRushOrder())
            return true;
        return false;
    }



}
