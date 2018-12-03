package android.mvvm.mg.com.mvvm_android.fragments.cards.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentCardsBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.cards.viewModel.CardsViewModel;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CardsFragment extends BaseFragment<CardsViewModel> {

    private FragmentCardsBinding mCardsBinding;

    public CardsFragment() {
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        mCardsBinding = FragmentCardsBinding.inflate(inflater, container, false);

        init();

        return mCardsBinding.getRoot();
    }

    private void init() {
        setTitle(R.string.cards_title);

        mViewModel = ViewModelProviders.of(this).get(CardsViewModel.class);
        mCardsBinding.setViewModel(mViewModel);

        subscribes();
    }

    private void subscribes() {
        mViewModel.loadData().observe(mActivity, cardList -> mViewModel.initRecycleViewData(cardList));
        handleRequest(mViewModel.getCards(), new IBaseRequestListener<Card>() {
            @Override
            public void onSuccessList(final List<Card> cardList) {
                mViewModel.insertAll(cardList);
            }
        });
    }
}
