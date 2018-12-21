package android.mvvm.mg.com.mvvm_android.core.base;

import java.util.HashMap;

public interface IBaseError {

    String getMessage();

    HashMap<String, String> getErrors();
}
