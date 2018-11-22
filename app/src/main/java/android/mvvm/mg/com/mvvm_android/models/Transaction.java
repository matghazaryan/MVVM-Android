package android.mvvm.mg.com.mvvm_android.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Transaction {

    private List<Transaction> transactionList;

    @JsonProperty("label")
    private String label;

    @JsonProperty("value")
    private String value;

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
