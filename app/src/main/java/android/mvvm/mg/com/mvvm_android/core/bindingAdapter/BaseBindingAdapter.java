package android.mvvm.mg.com.mvvm_android.core.bindingAdapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

public class BaseBindingAdapter {

    @BindingAdapter("baseVisibleView")
    public static void baseVisibleView(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:checked")
    public static void setChecked(final AppCompatCheckBox checkBox, final Boolean isChecked) {
        checkBox.setChecked(isChecked != null ? isChecked : false);
    }

    @BindingAdapter("baseFadAnim")
    public static void baseFadAnim(final View view, final boolean isVisible) {
        boolean isFirstTime = false;
        if (view.getTag() == null) {
            isFirstTime = true;
        }
        view.setTag("");

        if (isFirstTime) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } else {
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
                final ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                anim.setDuration(300);
                anim.start();
            } else {
                view.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }
}