package Entity;

public class Client extends User {
    protected String bankName;
    protected String bankNumber;
    protected boolean isSupplier;

    public Client(String lastName, String firstName, String username, String password, String bankName, String bankNumber, boolean isSupplier) {
        super(lastName, firstName, username, password, false);
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.isSupplier = isSupplier;
    }
    public Client(String username, String bankName, String bankNumber, boolean isSupplier) {
        this.username = username;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.isSupplier = isSupplier;
    }
    public Client(User u, Client c){
        this(u.lastName, u.firstName, u.username, u.password, c.bankName, c.bankNumber, c.isSupplier);
    }

    public Client() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    @Override
    public String toString() {
        return ""+firstName+" "+lastName+'\n'+
                "Username: "+username+'\n'+
                "Bank name: "+bankName+'\n'+
                "Bank account number: "+bankNumber+'\n';
    }
}
