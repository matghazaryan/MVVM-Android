package android.mvvm.mg.com.mvvm_android.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Configs {

    @JsonProperty("call_center")
    private String callCenter;

    public String getCallCenter() {
        return callCenter;
    }
}
