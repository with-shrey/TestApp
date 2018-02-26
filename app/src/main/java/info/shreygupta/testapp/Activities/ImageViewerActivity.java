package info.shreygupta.testapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import info.shreygupta.testapp.R;
import info.shreygupta.testapp.Utilities.GlideHelper;

public class ImageViewerActivity extends AppCompatActivity {
    ArrayList<String> images;
    ViewPager imagesPager;
    TextView selected, total;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        images = getIntent().getStringArrayListExtra("images");
        init();
        total.setText(String.valueOf(images.size()));
    }

    void init() {
        back = findViewById(R.id.back_button);
        selected = findViewById(R.id.selected);
        total = findViewById(R.id.total);
        imagesPager = findViewById(R.id.images_pager);
        ImagesPagerAdapter adapter = new ImagesPagerAdapter(this);
        imagesPager.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imagesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selected.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        selected.setText(String.valueOf(imagesPager.getCurrentItem() + 1));
    }

    class ImagesPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        ImagesPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.image_viewer_item, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageView);
            GlideHelper.load(ImageViewerActivity.this, images.get(position), imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
