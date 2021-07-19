package transaction;

import stocks.Stock;
import user.User;

import java.util.List;

public abstract class LMTTransaction extends AllTransactionsKinds
{
    public LMTTransaction(Stock stock, int limit, int numOfStocks, User initiator,String kind) {
        super(stock, limit, numOfStocks,initiator,kind);
    }

    public LMTTransaction() {
    }

    @Override
    protected abstract List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock m_Stock);

    @Override
    protected abstract boolean compareTransactionPrice(Transaction transaction);

    @Override
    public String getTransactionKind() {
        return "LMT";
    }
}

