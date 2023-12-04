package subsystem;

//import entity.payment.CreditCard;

import entity.payment.PaymentTransaction;
import subsystem.vnPay.VnPaySubsystemController;

import java.io.IOException;
import java.util.Map;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 *
 * @author hieud
 *
 */
public class VnPaySubsystem implements VnPayInterface {

    /**
     * Represent the controller of the subsystem.
     */
    private VnPaySubsystemController ctrl;

    /**
     * Initializes a newly created {@code InterbankSubsystem} object so that it
     * represents an Interbank subsystem.
     */
    public VnPaySubsystem() {
        String str = new String();
        this.ctrl = new VnPaySubsystemController();
    }

    /**
     * @see VnPayInterface#payOrder(entity.payment.CreditCard, int,
     * java.lang.String)
     */
    public String generatePayUrl(int amount) {

        try {
            return ctrl.generatePayOrderUrl(amount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see VnPayInterface#refund(entity.payment.CreditCard, int,
     * java.lang.String)
     */
    public PaymentTransaction refund(int amount, String contents) {
        PaymentTransaction transaction = ctrl.refund(amount, contents);
        return transaction;
    }

    public PaymentTransaction makePaymentTransaction(Map<String, String> response) {
        return ctrl.makePaymentTransaction(response);
    }
}
