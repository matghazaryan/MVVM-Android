package android.mvvm.mg.com.mvvm_android.models;


import java.util.HashMap;

public class RequestError {

    private String status;
    private String message;
    private HashMap<String, String> errors;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }
}
