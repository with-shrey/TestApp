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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.shreygupta.testapp.Adapters.CategoriesAdapter;
import info.shreygupta.testapp.Models.Configurations;
import info.shreygupta.testapp.Models.Variant;
import info.shreygupta.testapp.R;
import info.shreygupta.testapp.Utilities.AnimHelper;
import info.shreygupta.testapp.Utilities.ApiConfiguration;
import info.shreygupta.testapp.Utilities.GlideHelper;
import info.shreygupta.testapp.Utilities.VolleySingleton;

public class ProductDetailsActivity extends AppCompatActivity {
    ArrayList<Configurations> list;
    CategoriesAdapter adapter;
    TextView description, longDesc, title, price, priceText;
    ImageView headerImage;
    RecyclerView configRecycler;
    ImageView arrow;
    ProgressDialog dialog;
    ArrayList<Variant> variants;
    ArrayList<String> images;
    TextView seeMore;
    FloatingActionButton viewImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_details);
        init();
        if (VolleySingleton.getInstance(this).getConnected())
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
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewImage = findViewById(R.id.view_images);
        seeMore = findViewById(R.id.see_more);
        configRecycler = findViewById(R.id.product_options);
        images = new ArrayList<>();
        list = new ArrayList<>();
        variants = new ArrayList<>();
        configRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoriesAdapter(this, list);
        description = findViewById(R.id.description);
        longDesc = findViewById(R.id.features_description);
        title = findViewById(R.id.title);
        headerImage = findViewById(R.id.details_header_image);
        configRecycler.setAdapter(adapter);
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
                if (images.size() > 0) {
                    Intent intent = new Intent(ProductDetailsActivity.this, ImageViewerActivity.class);
                    intent.putStringArrayListExtra("images", images);
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
        adapter.setOnSelectionListner(new CategoriesAdapter.OnSelectedListner() {
            @Override
            public void onSelectionChanged(int position, int index) {
                setPrices();
            }
        });
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

    /**
     * Fetch All Categeries Eg:- Colours, Papers
     */
    private void fetchCategoriesAndDesc() {
        showProgressDialog("Fetching Product Info ..");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiConfiguration.productInfo(getIntent().getStringExtra("sku")), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    title.setText(object.getString("name"));
                    JSONArray configs = object.getJSONObject("extension_attributes").getJSONArray("configurable_product_options");
                    int n = configs.length();
                    for (int i = 0; i < n; i++) {
                        Configurations configurations = new Configurations();
                        JSONObject temp = configs.getJSONObject(i);
                        configurations.setTitle(temp.getString("label"));
                        JSONArray values = temp.getJSONArray("values");
                        int val = values.length();
                        ArrayList<String> data = new ArrayList<>();
                        for (int j = 0; j < val; j++) {
                            JSONObject value = values.getJSONObject(j);
                            data.add(value.getJSONObject("extension_attributes").getString("label"));
                        }
                        configurations.setOptions(data);
                        configurations.setSelected(0);
                        list.add(configurations);
                    }

                    JSONArray mediaImages = object.getJSONArray("media_gallery_entries");
                    int imgs = mediaImages.length();
                    for (int x = 0; x < imgs; x++) {
                        JSONObject temp = mediaImages.getJSONObject(x);
                        images.add(ApiConfiguration.IMAGE_PATH + temp.getString("file"));
                    }
                    JSONArray descArray = object.getJSONArray("custom_attributes");
                    int len = descArray.length();
                    for (int i = 0; i < len; i++) {
                        switch (descArray.getJSONObject(i).getString("attribute_code")) {
                            case "description":
                                String longDes = Html.fromHtml(descArray.getJSONObject(i).getString("value")).toString().trim()
                                        .replace("</li>", "")
                                        .replace("<ul>", "")
                                        .replace("</ul>", "")
                                        .replace("\n", "")
                                        .replace("<li>", "\n" + getString(R.string.Bullet) + " ");
                                description.setText(longDes);
                                break;
                            case "short_description":
                                longDes = Html.fromHtml(descArray.getJSONObject(i).getString("value")).toString().trim()
                                        .replace("</li>", "")
                                        .replace("<ul>", "")
                                        .replace("</ul>", "")
                                        .replace("\n", "")
                                        .replace("<li>", "\n" + getString(R.string.Bullet) + " ");

                                longDesc.setText(longDes);
                                break;
                        }
                    }
                    if (TextUtils.isEmpty(description.getText().toString())) {
                        description.setText(getString(R.string.not_available));
                    }
                    if (TextUtils.isEmpty(longDesc.getText().toString())) {
                        longDesc.setText(getString(R.string.not_available));
                    }
                    if (dialog != null)
                        dialog.dismiss();
                    fetchPrices();
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject errorObj = new JSONObject(response);
                        Toast.makeText(ProductDetailsActivity.this, errorObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();
                if (dialog != null)
                    dialog.dismiss();
            }
        }, VolleySingleton.getInstance(this).errorListener(dialog));


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    void fetchPrices() {
        showProgressDialog("Fetching Prices ..");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiConfiguration.productDetails(getIntent().getStringExtra("sku")), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    JSONArray object = new JSONArray(response);
                    int n = object.length();
                    for (int i = 0; i < n; i++) {
                        Variant variant = new Variant();
                        JSONObject temp = object.getJSONObject(i);
                        variant.setSku(temp.getString("sku"));
                        variant.setPrice(temp.getString("price"));
                            JSONArray descArray = temp.getJSONArray("custom_attributes");
                            int len = descArray.length();
                            for (int j = 0; j < len; j++) {
                                switch (descArray.getJSONObject(j).getString("attribute_code")) {
                                    case "price_description":
                                        variant.setPriceText(descArray.getJSONObject(j).getString("value"));
                                        break;
                                    case "image":
                                        variant.setImage(ApiConfiguration.IMAGE_PATH + descArray.getJSONObject(j)
                                                .getString("value"));
                                        break;
                                }
                            }
                        variants.add(variant);
                    }
                    setPrices();
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject errorObj = new JSONObject(response);
                        Toast.makeText(ProductDetailsActivity.this, errorObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();
                if (dialog != null)
                    dialog.dismiss();
            }
        }, VolleySingleton.getInstance(this).errorListener(dialog));


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * Find And Set The Price
     */
    void setPrices() {
        final String[] selectedConfigs = getSelectedConfigs();
        for (Variant variant : variants) {
            if (containsAll(variant.getSku(), selectedConfigs)) {
                priceText.setText(Html.fromHtml(variant.getPriceText()));
                price.setText(String.format("%s %s", getString(R.string.Rs), variant.getPrice()));
                GlideHelper.load(ProductDetailsActivity.this
                        , variant.getImage()
                        , headerImage);

            }
        }
        if (variants.size() == 0) {
            fetchPrices();
        }
    }

    /**
     * Get Selected Configurations from each time To Check with the sku in response
     *
     * @return String [] containing all Selected Strings
     */
    String[] getSelectedConfigs() {
        String[] sku = new String[list.size()];
        int i = 0;
        for (Configurations config : list) {
            String conf = config.getOptions().get(config.getSelected());
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
