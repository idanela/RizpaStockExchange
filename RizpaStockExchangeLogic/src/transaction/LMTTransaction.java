package transaction;

import stocks.Stock;
import user.User;

import java.util.List;

public abstract class LMTTransaction extends AllTransactionsKinds
{
    public LMTTransaction(Stock stock, int limit, int numOfStocks, User initiator) {
        super(stock, limit, numOfStocks,initiator);
    }

    public LMTTransaction() {
    }

    @Override
    protected abstract List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock m_Stock, String name);

    @Override
    protected abstract boolean compareTransactionPrice(Transaction transaction);
}

