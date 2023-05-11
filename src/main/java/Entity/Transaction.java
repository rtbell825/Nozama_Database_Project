package Entity;

import java.util.Date;
import java.util.Random;

public class Transaction {

    private int transactionId;
    private Status curStatus;
    public enum Status{
        IN_CART, ORDER_SUBMITTED, SHIPPED, RECEIVED
    }
    private Date dateTime;
    private double total;
    private double subTotal;
    private double tax;
    private String streetAddress = "nan";
    private String city = "nan";
    private String state = "nan";
    private String zip = "nan";
    private String deliveryInstructions = "";

    public Transaction(int transactionId, Status status, Date dateTime, double total, double subTotal, double tax, String streetAddress, String city, String state, String zip, String deliveryInstructions) {
        this.transactionId = transactionId;
        this.curStatus = status;
        this.dateTime = dateTime;
        this.total = total;
        this.subTotal = subTotal;
        this.tax = tax;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.deliveryInstructions = deliveryInstructions;
    }
    public Transaction(Status status, Date dateTime, double subtotal){
        Random rand = new Random();
        this.transactionId = rand.nextInt(Integer.MAX_VALUE);
        this.curStatus = status;
        this.dateTime = dateTime;
        this.subTotal = subtotal;
        this.tax = subtotal*0.08;
        this.total = subtotal+tax;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
        this.tax = subTotal*0.08;
        this.total = subTotal+tax;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public Status getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Status curStatus) {
        this.curStatus = curStatus;
    }

    @Override
    public String toString() {
        return "Transaction on " + dateTime + "          id: " + transactionId +
                "\nTotal: $"+total+ " (Subtotal: $"+subTotal+", Tax: $"+tax+")\n"+
                "Status: "+curStatus+"\n"+
                "Shipping address: "+streetAddress+". "+city+", "+state+" "+zip;
    }
    public String supplierToString(){
        return "Transaction on " + dateTime + "          id: " + transactionId +
                "Status: "+curStatus+"\n"+
                "Shipping address: "+streetAddress+". "+city+", "+state+" "+zip;
    }
}
