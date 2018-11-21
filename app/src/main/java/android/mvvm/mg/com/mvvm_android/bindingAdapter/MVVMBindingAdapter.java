package android.mvvm.mg.com.mvvm_android.bindingAdapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.adapters.CardAdapter;
import android.mvvm.mg.com.mvvm_android.glide.GlideApp;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(final ImageView imageView, final String path) {
        GlideApp.with(imageView.getContext())
                .load(path)
                .centerCrop()
                .placeholder(R.drawable.placeholder_profile_photo)
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

    @BindingAdapter("initRecycleView")
    public static void initRecycleView(final RecyclerView recyclerView, final List<Card> cardList) {
        final Context context = recyclerView.getContext();
        if (recyclerView.getAdapter() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new CardAdapter(cardList != null ? cardList : new ArrayList<>()));
        } else {
            ((CardAdapter) recyclerView.getAdapter()).setCardList(cardList);
        }
    }

    @BindingAdapter(value = {"color1", "color2"})
    public static void makeBackground(final View view, final String color1, final String color2) {
        view.setBackground(MVVMUtils.changeDrawableColor(view.getContext(), color1, color2));
    }
}