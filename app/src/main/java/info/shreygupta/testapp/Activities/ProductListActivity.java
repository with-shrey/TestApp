package info.shreygupta.testapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.shreygupta.testapp.Adapters.ProductListAdapter;
import info.shreygupta.testapp.Models.ProductList;
import info.shreygupta.testapp.R;
import info.shreygupta.testapp.Utilities.ApiClient;
import info.shreygupta.testapp.Utilities.ApiConfiguration;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductListActivity extends AppCompatActivity {
    List<ProductList> products;
    ProductListAdapter adapter;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_list);
        init();
        /*
        Check Connectivity Before Fetching Data
         */
        if (ApiClient.getConnected(this))
            fetchProducts();
        else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    void init() {
        products = new ArrayList<>();
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductListAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Fetch List Of Products
     */
    void fetchProducts() {
        showProgressDialog("Fetching Product List..");
        ApiConfiguration apiService =
                ApiClient.getClient().create(ApiConfiguration.class);
        Call<List<ProductList>> listCall = apiService.getAllProducts();
        listCall.enqueue(new Callback<List<ProductList>>() {
            @Override
            public void onResponse(Call<List<ProductList>> call, retrofit2.Response<List<ProductList>> response) {
                products = response.body();
                adapter = new ProductListAdapter(ProductListActivity.this, products);
                recyclerView.setAdapter(adapter);
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ProductList>> call, Throwable t) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(ProductListActivity.this, "Failed To Process", Toast.LENGTH_SHORT).show();
            }
        });
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

