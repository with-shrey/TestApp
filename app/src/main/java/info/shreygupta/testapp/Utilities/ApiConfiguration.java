package info.shreygupta.testapp.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XCODER on 2/24/2018.
 */

public class ApiConfiguration {
    static final String API_SERVER = "http://dev.fotonicia.in";
    public static final String PRODUCT_CONFIGURATION = API_SERVER + "/rest/default/V1/products/Square%20Photo%20Books";
    public static final String PRODUCTS = API_SERVER + "/rest/default/V1/categories/2/products";
    public static final String PRODUCT_DETAILS = API_SERVER + "/rest/default/V1/configurable-products/Square%20Photo%20Books/children";
    public static final String IMAGE_PATH = API_SERVER + "/pub/media/catalog/product";

    /**
     * Encode Special Characters of sku
     *
     * @param sku
     * @return Well Formed URL
     */
    public static String productDetails(String sku) {
        String query = null;
        try {
            query = URLEncoder.encode(sku, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return API_SERVER + "/rest/default/V1/configurable-products/" + query + "/children";
    }

    public static String productInfo(String sku) {
        String query = null;
        try {
            query = URLEncoder.encode(sku, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return API_SERVER + "/rest/default/V1/products/" + query;
    }
}
