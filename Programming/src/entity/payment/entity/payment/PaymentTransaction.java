package entity.payment;

public class PaymentTransaction {
    private String errorCode;
    private String transactionId;
    private String transactionContent;
    private int amount;
    private String createdAt;

    public PaymentTransaction(String errorCode, String transactionId, String transactionContent,
                              int amount, String createdAt) {
        super();
        this.errorCode = errorCode;

        this.transactionId = transactionId;
        this.transactionContent = transactionContent;
        this.amount = amount;
        this.createdAt = createdAt;
    }


    /**
     * @return String
     */
    public String getErrorCode() {
        return errorCode;
    }

    public String getTransactionContent() {
        return transactionContent;
    }
}
