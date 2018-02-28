package info.shreygupta.testapp.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.shreygupta.testapp.Models.Product;
import info.shreygupta.testapp.R;

/**
 * Created by XCODER on 2/24/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.VH> {
    Context context;
    List<Product.ConfigurableProductOption> list;
    OnSelectedListner listner;

    public CategoriesAdapter(Context context, List<Product.ConfigurableProductOption> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CategoriesAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.product_option_item, parent, false);
        return new CategoriesAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.VH holder, int position) {
        Product.ConfigurableProductOption configuration = list.get(position);
        holder.title.setText(configuration.getLabel() + " :");
        try {
            holder.selected.setText(configuration.getValues().get(configuration.getSelected()).getExtensionAttributes().getLabel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.optionsRecycler.setAdapter(new CategoryItemAdapter(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnSelectionListner(OnSelectedListner listner) {
        this.listner = listner;
    }

    public interface OnSelectedListner {
        void onSelectionChanged(int position, int index);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView selected;
        RecyclerView optionsRecycler;

        public VH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.property);
            selected = itemView.findViewById(R.id.selected);
            optionsRecycler = itemView.findViewById(R.id.description);
            optionsRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.VH> {
        int pos;
        List<Product.Value> data;

        public CategoryItemAdapter(int position) {
            pos = position;
            data = list.get(pos).getValues();
        }

        @Override
        public CategoryItemAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.category_item, parent, false);
            return new CategoryItemAdapter.VH(v);
        }

        @Override
        public void onBindViewHolder(CategoryItemAdapter.VH holder, int position) {
            try {
                holder.item.setText(data.get(position).getExtensionAttributes().getLabel());
                if (list.get(pos).getSelected() == position) {
                    holder.item.setBackgroundColor(context.getResources().getColor(R.color.green));
                    holder.item.setTextColor(context.getResources().getColor(android.R.color.white));
                } else {
                    holder.item.setBackground(context.getResources().getDrawable(R.drawable.box_border));
                    holder.item.setTextColor(context.getResources().getColor(R.color.green));
                }
                holder.item.setPadding((int) context.getResources().getDimension(R.dimen.padding_cat_button)
                        , (int) context.getResources().getDimension(R.dimen.padding_cat_button)
                        , (int) context.getResources().getDimension(R.dimen.padding_cat_button)
                        , (int) context.getResources().getDimension(R.dimen.padding_cat_button));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView item;

            public VH(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.category_text);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (list.get(pos).getSelected() != getAdapterPosition()) {
                    list.get(pos).setSelected(getAdapterPosition());
                    notifyDataSetChanged();
                    CategoriesAdapter.this.notifyItemChanged(pos);
                    if (listner != null) {
                        listner.onSelectionChanged(pos, getAdapterPosition());
                    }
                }
            }
        }
    }

}
