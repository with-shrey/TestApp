package info.shreygupta.testapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import info.shreygupta.testapp.Activities.ProductDetailsActivity;
import info.shreygupta.testapp.Models.Product;
import info.shreygupta.testapp.R;

/**
 * Created by XCODER on 2/24/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.VH> {

    Context context;
    ArrayList<Product> list;

    public ProductListAdapter(Context context, ArrayList<Product> data) {
        this.context = context;
        list = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.product_list, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.title.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public VH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("sku", list.get(getAdapterPosition()).getSku());
            context.startActivity(intent);
        }
    }
}
