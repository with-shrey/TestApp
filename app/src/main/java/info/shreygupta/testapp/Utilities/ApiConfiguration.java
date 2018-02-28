package info.shreygupta.testapp.Utilities;

import java.util.List;

import info.shreygupta.testapp.Models.Product;
import info.shreygupta.testapp.Models.ProductList;
import info.shreygupta.testapp.Models.Variant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
/**
 * Created by XCODER on 2/24/2018.
 */

public interface ApiConfiguration {
    String API_SERVER = "http://dev.fotonicia.in";
    String IMAGE_PATH = API_SERVER + "/pub/media/catalog/product";

    @GET("rest/default/V1/products/{sku}")
    Call<Product> getProduct(@Path("sku") String sku);

    @GET("rest/default/V1/configurable-products/{sku}/children")
    Call<List<Variant>> getVariants(@Path("sku") String sku);

    @GET("rest/default/V1/categories/2/products")
    Call<List<ProductList>> getAllProducts();
}
