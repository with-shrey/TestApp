package info.shreygupta.testapp.Models;

/**
 * Created by XCODER on 2/26/2018.
 */

public class Variant {
    String price;
    String priceText;
    String image;

    public Variant(String price, String priceText, String image) {
        this.price = price;
        this.priceText = priceText;
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceText() {
        return priceText;
    }

    public String getImage() {
        return image;
    }
}
