package info.shreygupta.testapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.shreygupta.testapp.Adapters.ProductListAdapter;
import info.shreygupta.testapp.Models.Product;
import info.shreygupta.testapp.R;
import info.shreygupta.testapp.Utilities.ApiConfiguration;
import info.shreygupta.testapp.Utilities.VolleySingleton;

public class ProductListActivity extends AppCompatActivity {
    ArrayList<Product> products;
    ProductListAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_list);
        init();
        /*
        Check Connectivity Before Fetching Data
         */
        if (VolleySingleton.getInstance(this).getConnected())
            fetchProducts();
        else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    void init() {
        products = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductListAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Fetch List Of Products
     */
    void fetchProducts() {
        showProgressDialog("Fetching Product List ..");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiConfiguration.PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    JSONArray main = new JSONArray(response);
                    int n = main.length();
                    for (int i = 0; i < n; i++) {
                        JSONObject object = main.getJSONObject(i);
                        JSONObject extension = object.getJSONObject("extension_attributes");
                        products.add(new Product(object.getString("sku"), extension.getString("name")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject errorObj = new JSONObject(response);
                        Toast.makeText(ProductListActivity.this, errorObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        }, VolleySingleton.getInstance(this).errorListener(dialog));


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    /**
     * Show Progress Dialog While Fetching Data
     *
     * @param message The Text To Diaplay On Dialog
     */
    void showProgressDialog(String message) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }
}

