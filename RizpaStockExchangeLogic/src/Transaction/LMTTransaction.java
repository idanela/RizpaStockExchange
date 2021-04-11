package Transaction;

import Stocks.Stock;

import java.util.List;

public abstract class LMTTransaction extends AllTransactionsKinds
{
    public LMTTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock, limit, numOfStocks);
    }

    public LMTTransaction() {
    }

    @Override
    protected abstract List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock m_Stock);

    @Override
    protected abstract boolean compareTransactionPrice(ITransaction transaction);
}

