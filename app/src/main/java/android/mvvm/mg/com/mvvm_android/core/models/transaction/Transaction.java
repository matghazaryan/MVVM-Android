package android.mvvm.mg.com.mvvm_android.core.models.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    @JsonProperty("label")
    private String label;

    @JsonProperty("value")
    private String value;

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
