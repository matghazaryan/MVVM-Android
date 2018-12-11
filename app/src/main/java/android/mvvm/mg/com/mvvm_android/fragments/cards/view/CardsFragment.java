package android.mvvm.mg.com.mvvm_android.fragments.cards.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentCardsBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.cards.viewModel.CardsViewModel;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;

import java.util.List;

public class CardsFragment extends BaseFragment<CardsViewModel, FragmentCardsBinding> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_cards;
    }

    @Override
    protected Class<CardsViewModel> getViewModelClass() {
        return CardsViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentCardsBinding binding, final CardsViewModel viewModel) {
        binding.setViewModel(viewModel);
    }

    @Override
    public int getTitleRes() {
        return R.string.cards_title;
    }

    @Override
    public void subscribes(final LifecycleOwner owner) {
        mViewModel.loadData().observe(owner, cardList -> mViewModel.initRecycleViewData(cardList));
        handleRequest(mViewModel.getCards(), new IBaseRequestListener<Card>() {
            @Override
            public void onSuccessList(final List<Card> cardList) {
                mViewModel.insertAll(cardList);
            }
        });
    }
}
