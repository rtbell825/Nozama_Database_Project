package Entity;

public class Purchase {
    private int transactionId;
    private int productId;
    private String buyerUsername;
    private int quantity;

    public Purchase(int transactionId, int productId, String buyerUsername, int quantity) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.buyerUsername = buyerUsername;
        this.quantity = quantity;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "\t"+quantity+" ";
    }
}
