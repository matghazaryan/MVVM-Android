package android.mvvm.mg.com.mvvm_android.ui.fragments.cards.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIRequestListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseOfflineFragment;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.listeners.IEmptyViewHandler;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentCardsBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.cards.viewModel.CardsViewModel;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class CardsFragment extends DMBaseOfflineFragment<CardsViewModel, FragmentCardsBinding> implements IEmptyViewHandler {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_cards;
    }

    @Override
    protected Class<CardsViewModel> getViewModelClass() {
        return CardsViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentCardsBinding binding, final CardsViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.cards_title;
    }

    @Override
    public void subscribers(final LifecycleOwner owner) {
        mViewModel.<String>getAction(IMVVMConstants.Action.SHOW_TOAST).observe(owner, text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show());
        mViewModel.dbCards().observe(owner, cardList -> mViewModel.setBaseList(cardList));
        handleRequestFor(mViewModel.apiCards(), new DMBaseIRequestListener<Card>() {
            @Override
            public void onSuccessList(final List<Card> cardList) {
                mViewModel.insertAll(cardList);
            }
        });
    }

    @Override
    public void onEmptyViewClick(final View view) {
        Toast.makeText(getContext(), getString(R.string.card_on_empty_click), Toast.LENGTH_SHORT).show();
    }
}
