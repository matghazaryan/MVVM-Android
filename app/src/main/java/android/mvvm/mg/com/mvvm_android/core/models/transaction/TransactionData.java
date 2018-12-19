package android.mvvm.mg.com.mvvm_android.core.models.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TransactionData {

    @JsonProperty("transactions")
    private List<TransactionGroup> transactionGroupList;

    @JsonProperty("next_page")
    private int nextPage;


    public List<TransactionGroup> getTransactionList() {
        return transactionGroupList;
    }

    public int getNextPage() {
        return nextPage;
    }
}
