package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class LMTSellTransaction extends AllTransactionsKinds{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks, User initiator) {
        super(stock,limit,numOfStocks,initiator);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= m_Limit)
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }

}
