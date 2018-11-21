package android.mvvm.mg.com.mvvm_android.adapters;

import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.CardItemBinding;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<Card> cardList;
    private LayoutInflater inflater;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardItemBinding binding;

        MyViewHolder(final CardItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public CardAdapter(final List<Card> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        final CardItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.card_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        holder.binding.setCard(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void setCardList(final List<Card> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }
}
