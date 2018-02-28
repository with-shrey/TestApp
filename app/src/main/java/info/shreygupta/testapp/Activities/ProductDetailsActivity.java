package info.shreygupta.testapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import info.shreygupta.testapp.Adapters.CategoriesAdapter;
import info.shreygupta.testapp.Models.Product;
import info.shreygupta.testapp.Models.Variant;
import info.shreygupta.testapp.R;
import info.shreygupta.testapp.Utilities.AnimHelper;
import info.shreygupta.testapp.Utilities.ApiClient;
import info.shreygupta.testapp.Utilities.ApiConfiguration;
import info.shreygupta.testapp.Utilities.GlideHelper;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductDetailsActivity extends AppCompatActivity {
    Product product;
    CategoriesAdapter adapter;
    TextView description, longDesc, title, price, priceText;
    ImageView headerImage;
    RecyclerView configRecycler;
    ImageView arrow;
    ProgressDialog dialog;
    List<Variant> variants;
    TextView seeMore;
    FloatingActionButton viewImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_details);
        init();
        if (ApiClient.getConnected(this))
            fetchCategoriesAndDesc();
        else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Initialize Views And Listners
     */
    void init() {
        /*
        Top Back Button Click Listner
         */
        product = new Product();
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewImage = findViewById(R.id.view_images);
        seeMore = findViewById(R.id.see_more);
        configRecycler = findViewById(R.id.product_options);
        variants = new ArrayList<>();
        configRecycler.setLayoutManager(new LinearLayoutManager(this));
        description = findViewById(R.id.description);
        longDesc = findViewById(R.id.features_description);
        title = findViewById(R.id.title);
        headerImage = findViewById(R.id.details_header_image);
        arrow = findViewById(R.id.price_arrow);
        price = findViewById(R.id.price);
        priceText = findViewById(R.id.price_text);
        findViewById(R.id.price_layout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleCategories();
            }
        });
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getMediaGalleryEntries().size() > 0) {
                    Intent intent = new Intent(ProductDetailsActivity.this, ImageViewerActivity.class);
                    intent.putStringArrayListExtra("images", product.getImagesList());
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fab_background)));

        /*
        Custom Listner To Listner for changes in variant selection in adapter
         */
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCollapseDesc();
            }
        });
    }

    /**
     * Expand/Collapse Long Description
     */
    void expandCollapseDesc() {
        if (seeMore.getText().toString().equals(getString(R.string.see_more))) {
            longDesc.setMaxLines(Integer.MAX_VALUE);
            seeMore.setText(R.string.show_lines);
        } else {
            longDesc.setMaxLines(4);
            seeMore.setText(R.string.see_more);
        }
    }

    /**
     * Change The Visibility Of Product Variants
     */
    void toggleCategories() {
        AnimHelper animationUtils = new AnimHelper(ProductDetailsActivity.this);
        if (configRecycler.getVisibility() == View.VISIBLE) {
            animationUtils.upAnimation(configRecycler);
            arrow.setRotation(0);
        } else {
            animationUtils.dropAnimation(configRecycler);
            arrow.setRotation(180);
        }
    }

    void setTitleAndDesc() {
        title.setText(product.getName());
        description.setText(product.getCustomAttributes("description").replace("\\u2022", getString(R.string.Bullet)));
        longDesc.setText(product.getCustomAttributes("short_description").replace("\\u2022", getString(R.string.Bullet)));
    }

    /**
     * Fetch All Categeries Eg:- Colours, Papers
     */
    private void fetchCategoriesAndDesc() {
        showProgressDialog("Fetching Product Info ..");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Product.CustomAttribute.class, new Product.CustomAttrValueTypeAdapter()).create();
        ApiConfiguration apiService =
                ApiClient.getClient(gson).create(ApiConfiguration.class);
        Call<Product> productCall = apiService.getProduct(getIntent().getStringExtra("sku"));

        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, retrofit2.Response<Product> response) {
                product = response.body();
                adapter = new CategoriesAdapter(ProductDetailsActivity.this, product.getExtensionAttributes().getConfigurableProductOptions());
                configRecycler.setAdapter(adapter);
                setTitleAndDesc();
                adapter.setOnSelectionListner(new CategoriesAdapter.OnSelectedListner() {
                    @Override
                    public void onSelectionChanged(int position, int index) {
                        setPrices();
                    }
                });
                if (dialog != null)
                    dialog.dismiss();
                fetchPrices();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                if (dialog != null)
                    dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(ProductDetailsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void fetchPrices() {
        showProgressDialog("Fetching Product Info ..");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Variant.CustomAttribute.class, new Variant.CustomAttrValueTypeAdapter()).create();
        ApiConfiguration apiService =
                ApiClient.getClient(gson).create(ApiConfiguration.class);
        Call<List<Variant>> variantCall = apiService.getVariants(getIntent().getStringExtra("sku"));
        variantCall.enqueue(new Callback<List<Variant>>() {
            @Override
            public void onResponse(Call<List<Variant>> call, retrofit2.Response<List<Variant>> response) {
                variants = response.body();
                setPrices();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Variant>> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                t.printStackTrace();
                Toast.makeText(ProductDetailsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Find And Set The Price
     */
    void setPrices() {
        final String[] selectedConfigs = getSelectedConfigs();
        for (Variant variant : variants) {
            if (containsAll(variant.getSku(), selectedConfigs)) {
                priceText.setText(Html.fromHtml(variant.getCustomAttributes("price_description")));
                price.setText(String.format("%s %s", getString(R.string.Rs), variant.getPrice()));
                GlideHelper.load(ProductDetailsActivity.this
                        , ApiConfiguration.IMAGE_PATH + variant.getCustomAttributes("image")
                        , headerImage);

            }
        }
    }

    /**
     * Get Selected Configurations from each time To Check with the sku in response
     *
     * @return String [] containing all Selected Strings
     */
    String[] getSelectedConfigs() {
        String[] sku = new String[product.getExtensionAttributes().getConfigurableProductOptions().size()];
        int i = 0;
        for (Product.ConfigurableProductOption config : product.getExtensionAttributes().getConfigurableProductOptions()) {
            String conf = config.getValues().get(config.getSelected()).getExtensionAttributes().getLabel();
            // If selected Configuration Has No Matt replace it with 0.5 X 0.5
            if (conf.equals("No Matt")) {
                conf = "0.5\"x0.5\"";
            }
            sku[i++] = conf;
        }
        return sku;
    }

    /**
     * Check all keywords exists in word
     *
     * @param word     String To Check In
     * @param keywords Strings
     * @return true if contains All keywords
     */
    boolean containsAll(String word, String[] keywords) {
        boolean containsAll = true;
        for (String keyword : keywords) {
            if (!word.contains(keyword)) {
                containsAll = false;
                break;
            }
        }
        return containsAll;
    }

    void showProgressDialog(String message) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }
}
