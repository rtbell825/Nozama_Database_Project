package Entity;

import java.util.Random;

public class Product {
    private int productId;
    private String sellerUsername;
    private double price;
    private int quantity;
    private double rating;
    private String description;
    private int pageId;

    public Product(int productId, String sellerUsername, double price, int quantity, double rating, String description, int pageId) {
        this.productId = productId;
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.quantity = quantity;
        this.rating = rating;
        this.description = description;
        this.pageId = pageId;
    }

    public Product(String sellerUsername, double price, int quantity, double rating, String description, int pageId) {
        Random rand = new Random();
        this.productId = rand.nextInt(Integer.MAX_VALUE);
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.quantity = quantity;
        this.rating = rating;
        this.description = description;
        this.pageId = pageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return ""+pageId+": "+description+'\n'+
                "$"+price+"     Rating:"+rating+"\n"
                ;
    }
    public String purchasedToString() {
        return ""+description+". $"+price+" each. Seller: "+sellerUsername;
    }
    public String cartToString(){
        return ""+description+"\n$"+price+"\tRating:"+rating+"\nSeller: "+sellerUsername;
    }
    public String viewToString(){
        return ""+description+"\n$"+price+"\tRating:"+rating+"\nStock:"+quantity+"\nSeller:"+sellerUsername;
    }
}
