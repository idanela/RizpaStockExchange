package transaction;

import stocks.Stock;
import user.User;

import java.util.List;

public abstract class MKTTransaction extends AllTransactionsKinds {
    public MKTTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks, User initiator,String kind) {
        super(m_Stock, m_Limit, m_NumOfStocks,initiator,kind);
    }

    public MKTTransaction() {
    }

    @Override
    protected abstract List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock m_Stock);
    protected abstract boolean compareTransactionPrice(Transaction transaction);

    @Override
    public String getTransactionKind() {
        return "MKT";
    }
}
