package android.mvvm.mg.com.mvvm_android.adapters;

import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.CardItemBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseOnItemClickListener;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;
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
