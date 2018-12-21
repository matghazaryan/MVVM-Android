package android.mvvm.mg.com.mvvm_android.core.models.error;


import android.mvvm.mg.com.mvvm_android.core.base.IBaseError;

import java.util.HashMap;

public class RequestError implements IBaseError {

    private String status;
    private String message;
    private HashMap<String, String> errors;

    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HashMap<String, String> getErrors() {
        return errors;
    }
}
