package android.mvvm.mg.com.mvvm_android.ui.fragments.language.view;

import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseFragment;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLanguageBinding;
import android.mvvm.mg.com.mvvm_android.databinding.LanguageLayoutBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.language.viewModel.LanguageViewModel;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class LanguageFragment extends DMBaseFragment<LanguageViewModel, FragmentLanguageBinding> {

    private final List<LanguageLayoutBinding> mLanguageLayoutBindingList = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_language;
    }

    @Override
    protected Class<LanguageViewModel> getViewModelClass() {
        return LanguageViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentLanguageBinding binding, final LanguageViewModel viewModel) {
        binding.setViewModel(viewModel);
    }

    @Override
    public FragmentActivity getActivityForViewModel() {
        return mActivity;
    }

    @Override
    public int getTitleRes() {
        return R.string.language_title;
    }

    @Override
    public void initialize() {
        initLanguageViews();
    }

    private void initLanguageViews() {
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_armenian), IMVVMConstants.Language.HY));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_english), IMVVMConstants.Language.EN));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_russian), IMVVMConstants.Language.RU));
    }

    private LanguageLayoutBinding createLanguageView(final String name, final String code) {
        final LanguageLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.language_layout, null, false);
        binding.setName(name);
        binding.setIsVisible(code.equalsIgnoreCase(mViewModel.getLanguageCode()));
        binding.setHandler((View) -> changeLanguage(binding, code));

        mBinding.llLanguage.addView(binding.getRoot());

        return binding;
    }

    private void changeLanguage(final LanguageLayoutBinding layoutBinding, final String code) {
        for (final LanguageLayoutBinding binding : mLanguageLayoutBindingList) {
            binding.setIsVisible(false);
        }
        layoutBinding.setIsVisible(true);
        mViewModel.saveLanguageCode(code);
        mActivity.recreate();
    }
}
