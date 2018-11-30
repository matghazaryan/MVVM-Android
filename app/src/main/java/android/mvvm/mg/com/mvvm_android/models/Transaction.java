package android.mvvm.mg.com.mvvm_android.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Transaction {

    @JsonProperty("transactions")
    private List<Transaction> transactionList;

    @JsonProperty("transaction_details")
    private List<Transaction> transactionDetailsList;

    @JsonProperty("label")
    private String label;

    @JsonProperty("value")
    private String value;

    @JsonProperty("next_page")
    private int nextPage;


    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public List<Transaction> getTransactionDetailsList() {
        return transactionDetailsList;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getNextPage() {
        return nextPage;
    }
}
