package controller;

import common.exception.PaymentException;
import common.exception.TransactionNotDoneException;
import common.exception.UnrecognizedException;
import entity.cart.Cart;
import subsystem.VnPayInterface;
import subsystem.VnPaySubsystem;

import java.util.Hashtable;
import java.util.Map;

/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 * @author hieud
 */
public class PaymentController extends BaseController {


    /**
     * Represent the Interbank subsystem
     */
    private VnPayInterface vnPayService;

    /**
     * Validate the input date which should be in the format "mm/yy", and then
     * return a {@link java.lang.String String} representing the date in the
     * required format "mmyy" .
     *
     * @param date - the {@link java.lang.String String} represents the input date
     * @return {@link java.lang.String String} - date representation of the required
     * format
     * @throws TransactionNotDoneException - if the string does not represent a valid date
     *                                     in the expected format
     */
//  private String getExpirationDate(String date) throws TransactionNotDoneException {
//    String[] strs = date.split("/");
//    if (strs.length != 2) {
//      throw new TransactionNotDoneException();
//    }
//
//    String expirationDate = null;
//    int month = -1;
//    int year = -1;
//
//    try {
//      month = Integer.parseInt(strs[0]);
//      year = Integer.parseInt(strs[1]);
//      if (month < 1 || month > 12 || year < Calendar.getInstance().get(Calendar.YEAR) % 100 || year > 100) {
//        throw new TransactionNotDoneException();
//      }
//      expirationDate = strs[0] + strs[1];
//
//    } catch (Exception ex) {
//      throw new TransactionNotDoneException();
//    }
//
//    return expirationDate;
//  }
//
//  /**
//   * Pay order, and then return the result with a message.
//   *
//   * @param amount         - the amount to pay
//   * @param contents       - the transaction contents
//
//   * @return {@link java.util.Map Map} represent the payment result with a
//   *         message.
//   */
    public Map<String, String> makePayment(Map<String, String> res) {
        Map<String, String> result = new Hashtable<String, String>();

        try {


            this.vnPayService = new VnPaySubsystem();
            var trans = vnPayService.makePaymentTransaction(res);

            result.put("RESULT", "PAYMENT SUCCESSFUL!");
            result.put("MESSAGE", "You have succesffully paid the order!");
        } catch (PaymentException | UnrecognizedException ex) {
            result.put("MESSAGE", ex.getMessage());
            result.put("RESULT", "PAYMENT FAILED!");

        }
        return result;
    }

    public void emptyCart() {
        Cart.getCart().emptyCart();
    }
}