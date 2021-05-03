package user;

import holding.Holding;
import transaction.Transaction;
import transaction.TransactionMade;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    Holding holding;
    List<TransactionMade> transactionsMade;
    List<Transaction> pendingBuyTransactions;
    List<Transaction> pendingSellTransactions;

    public User(String name, Holding holding) {
        this.name = name;
        this.holding = holding;
    }
}
