package android.mvvm.mg.com.mvvm_android.fragments.language.view;

import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLanguageBinding;
import android.mvvm.mg.com.mvvm_android.databinding.LanguageLayoutBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.language.viewModel.LanguageViewModel;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;

import java.util.ArrayList;
import java.util.List;

public class LanguageFragment extends BaseFragment<LanguageViewModel, FragmentLanguageBinding> {

    private final List<LanguageLayoutBinding> mLanguageLayoutBindingList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_language;
    }

    @Override
    protected Class<LanguageViewModel> getViewModelClass() {
        return LanguageViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentLanguageBinding binding, final LanguageViewModel viewModel) {
        binding.setViewModel(viewModel);
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
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_armenian), Language.HY));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_english), Language.EN));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.language_russian), Language.RU));
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
        MVVMUtils.updateLanguage(mActivity, code);
        setTitle(R.string.language_title);
    }
}
