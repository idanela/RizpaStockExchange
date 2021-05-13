package user;

import generated.RseUser;
import holding.Holding;
import transaction.Transaction;
import transaction.TransactionMade;

import java.util.List;

public class User {
    private String name;
    List<Holding> holdings;
    List<TransactionMade> transactionsMade;
    List<Transaction> pendingBuyTransactions;
    List<Transaction> pendingSellTransactions;

    public User(String name, List<Holding> holding) {
        this.name = name;
        this.holdings = holding;
    }

    public User(RseUser user) {
        this.name =user.getName();
        this.holdings = holdings;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals((String)obj);
    }
}
