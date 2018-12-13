package android.mvvm.mg.com.mvvm_android.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configs {

    @JsonProperty("call_center")
    private String callCenter;

    public String getCallCenter() {
        return callCenter;
    }
}
