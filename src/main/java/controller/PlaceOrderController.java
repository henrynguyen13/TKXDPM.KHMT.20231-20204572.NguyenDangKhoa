package controller;

import java.util.regex.Pattern;

public class PlaceOrderController {
    /*
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

    /*
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


    /*
     * @param address
     * @return boolean
     */
    public boolean validateAddressPlaceRushOrder(String address){
        if(address == null || address.isEmpty()) return false;
        String lowercaseAddress = address.toLowerCase();
        return lowercaseAddress.contains("hà nội");
    }


    /*
     * @param price
     * @param weight
     * @param address
     * @param quantityOfProductRushShipping
     * @return double
     */
    public double calculateShippingFee(double price, double weight, String address, int quantityOfProductRushShipping){

        if (price > 100000) {
            return 0.0;
        }

        double baseCost = 0;
        double baseWeight = 0;
        double additionalCostPerHalfKg = 0;

        if (address.toLowerCase().contains("hà nội") || address.toLowerCase().contains("hồ chí minh")) {
            baseCost = 22000;
            baseWeight = 3;
            additionalCostPerHalfKg = 2500;
        } else {
            baseCost = 30000;
            baseWeight = 0.5;
            additionalCostPerHalfKg = 2500;
        }

        double rushShippingCost = 10000 * quantityOfProductRushShipping;
        double regularShippingCost = 0;

        if(weight <= baseWeight){
            regularShippingCost = baseCost;
        }
        else{
            regularShippingCost = baseCost + Math.ceil((weight - baseWeight) * 2) * additionalCostPerHalfKg;
        }

        return rushShippingCost + regularShippingCost;
    }


}
