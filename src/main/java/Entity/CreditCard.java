package Entity;

import java.util.Date;

public class CreditCard {
    private String name;
    private String number;
    private Date expDate;
    private int securityCode;

    public CreditCard(String name, String number, Date expDate, int securityCode) {
        this.name = name;
        this.number = number;
        this.expDate = expDate;
        this.securityCode = securityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public String toString() {
        return "Credit Card ending in " +number.substring(12);
    }
}
