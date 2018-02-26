package info.shreygupta.testapp.Models;

/**
 * Created by XCODER on 2/26/2018.
 */

public class Variant {
    String sku;
    String price;
    String priceText;
    String image;

    /**
     * Default Constructor with default values
     */
    public Variant() {
        price = "\\u20B9 " + "-";
        priceText = " for - photos";
        image = "";
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
