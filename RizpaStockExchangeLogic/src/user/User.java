package user;

import transaction.Transaction;
import transaction.TransactionMade;

import java.util.List;

public class User {
    private String name;
    List<TransactionMade> transactionsMade;
    List<Transaction> pendingBuyTransactions;
    List<Transaction> pendingSellTransactions;

    public User(List<TransactionMade> transactionsMade, List<Transaction> pendingBuyTransactions, List<Transaction> pendingSellTransactions) {
        this.transactionsMade = transactionsMade;
        this.pendingBuyTransactions = pendingBuyTransactions;
        this.pendingSellTransactions = pendingSellTransactions;
    }
}
