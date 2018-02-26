package info.shreygupta.testapp.Models;

/**
 * Created by XCODER on 2/24/2018.
 */

public class Product {

    String name;
    String sku;

    public Product(String name, String sku) {
        this.name = name;
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }
}
