package info.shreygupta.testapp.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import info.shreygupta.testapp.R;

/**
 * Created by XCODER on 2/26/2018.
 */

public class AnimHelper {
    Context context;

    public AnimHelper(Context context) {
        this.context = context;
    }

    public void dropAnimation(final RecyclerView view) {
        Animation slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(slide_down);
    }

    public void upAnimation(final View view) {
        Animation slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(slide_up);
    }
}
