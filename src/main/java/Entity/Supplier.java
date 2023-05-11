package Entity;

import Entity.Client;

public class Supplier extends Client {
    private double rating;

    public Supplier(String lastName, String firstName, String username, String password, String bankName, String bankNumber, double rating) {
        super(lastName, firstName, username, password, bankName, bankNumber, true);
        this.rating = rating;
    }

    public Supplier(Client c, double rating){
        this(c.lastName, c.firstName, c.username, c.password, c.bankName, c.bankNumber, rating);
    }
    public Supplier(User u, Client c, double rating){
        this(u.lastName, u.firstName, u.username, u.password, c.bankName, c.bankNumber, rating);
    }

    public Supplier(String username, double rating) {
        this.username = username;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return ""+firstName+" "+lastName+'\n'+
                "Username: "+username+'\n'+
                "Rating: "+rating+'\n'+
                "Bank name: "+bankName+'\n'+
                "Bank account number: "+bankNumber+'\n';
    }
}
