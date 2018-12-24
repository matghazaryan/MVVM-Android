package android.mvvm.mg.com.mvvm_android.ui.fragments.cards.view;

import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.BaseAdapter;
import android.mvvm.mg.com.mvvm_android.core.base.IBaseOnItemClickListener;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.databinding.CardItemBinding;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

public class CardAdapter extends BaseAdapter<Card, CardItemBinding> {

    public CardAdapter(final List<Card> cardList, final IBaseOnItemClickListener<Card> listener) {
        super(cardList, listener);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.card_item;
    }

    @Override
    protected View onBaseBindViewHolder(final @NonNull BaseViewHolder<CardItemBinding> holder, final int position, final Card card) {
        holder.binding.setCard(card);
        return holder.binding.getRoot();
    }
}
