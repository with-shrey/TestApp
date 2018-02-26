package info.shreygupta.testapp.Utilities;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import info.shreygupta.testapp.R;

/**
 * Created by XCODER on 2/26/2018.
 */

public class GlideHelper {
    public static void load(Context context, String url, ImageView image) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);
        Glide.with(context).load(url).apply(requestOptions).into(image);
    }
}
