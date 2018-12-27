package android.mvvm.mg.com.mvvm_android.core.models.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class TransactionGroup {

    @JsonProperty("transaction_details")
    private List<Transaction> transactionList;

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
