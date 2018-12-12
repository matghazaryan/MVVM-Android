package android.mvvm.mg.com.mvvm_android.bindingAdapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.adapters.CardAdapter;
import android.mvvm.mg.com.mvvm_android.adapters.TransactionAdapter;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseOnItemClickListener;
import android.mvvm.mg.com.mvvm_android.glide.GlideApp;
import android.mvvm.mg.com.mvvm_android.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MVVMBindingAdapter {

    @BindingAdapter("setError")
    public static void setError(final MaterialEditText editText, final String error) {
        editText.setError(error);
    }

    @BindingAdapter("setVisibleView")
    public static void setVisibleView(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("setFadAnim")
    public static void setFadAnim(final View view, final boolean isVisible) {
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

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(final ImageView imageView, final String path) {
        GlideApp.with(imageView.getContext())
                .load(path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.placeholder_profile_photo)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter("setCardStatus")
    public static void setCardStatus(final TextView textView, final boolean isDefault) {
        final Context context = textView.getContext();
        final String text;
        if (isDefault) {
            text = context.getResources().getString(R.string.cards_default);
        } else {
            text = context.getResources().getString(R.string.cards_secondary);
        }

        textView.setText(text);
    }

    @BindingAdapter(value = {"initRecycleViewCardList", "listener"})
    public static void initRecycleViewCardList(final RecyclerView recyclerView, final List<Card> cardList, final IBaseOnItemClickListener<Card> listener) {
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new CardAdapter(cardList != null ? cardList : new ArrayList<>(), listener));
        } else {
            ((CardAdapter) recyclerView.getAdapter()).replaceList(cardList);
        }
    }

    @BindingAdapter("initRecycleViewTransactionList")
    public static void initRecycleViewTransactionList(final RecyclerView recyclerView, final TransactionData transactionData) {
        if (transactionData != null) {
            if (transactionData.getNextPage() == 0) {
                recyclerView.clearOnScrollListeners();
            }

            if (recyclerView.getAdapter() == null) {
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(new TransactionAdapter(transactionData.getTransactionList()));
            } else {
                ((TransactionAdapter) recyclerView.getAdapter()).addList(transactionData.getTransactionList());
            }
        }
    }

    @BindingAdapter(value = {"color1", "color2"})
    public static void makeBackground(final View view, final String color1, final String color2) {
        view.setBackground(MVVMUtils.changeDrawableColor(view.getContext(), color1, color2));
    }
}