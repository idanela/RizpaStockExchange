package user;

import transaction.Transaction;

import java.util.List;

public class User {
    private String name;
    private List<Transaction> m_PendingBuyTransactions;
    private List<Transaction> m_PendingSellTransactions;
    private List<Transaction> m_Transactions;

    public User(String name, List<Transaction> m_PendingBuyTransactions, List<Transaction> m_PendingSellTransactions, List<Transaction> m_Transactions) {
        this.name = name;
        this.m_PendingBuyTransactions = m_PendingBuyTransactions;
        this.m_PendingSellTransactions = m_PendingSellTransactions;
        this.m_Transactions = m_Transactions;
    }

    public String getName() {
        return name;
    }
}
