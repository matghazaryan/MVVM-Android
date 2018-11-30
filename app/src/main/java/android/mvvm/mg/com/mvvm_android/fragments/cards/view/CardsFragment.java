package android.mvvm.mg.com.mvvm_android.fragments.cards.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentCardsBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.cards.viewModel.CardsViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class CardsFragment extends BaseFragment<CardsViewModel> {

    private FragmentCardsBinding mCardsBinding;
    private CardsViewModel mViewModel;

    public CardsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        subscribeGetCards();

        mViewModel.loadData().observe(mActivity, cardList -> mViewModel.initRecycleViewData(cardList));
    }

    private void subscribeGetCards() {
        final DMLiveDataBag<Card, RequestError> liveDataBug = mViewModel.request();
        liveDataBug.getSuccessListT().observe(mActivity, cardSuccessListT -> mViewModel.insertAll(cardSuccessListT));
        liveDataBug.getErrorE().observe(mActivity, errorErrorE -> mViewModel.handleErrors(errorErrorE));
    }
}
