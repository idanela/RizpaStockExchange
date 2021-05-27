package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class LMTSellTransaction extends LMTTransaction{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks, User initiator,String kind) {
        super(stock,limit,numOfStocks,initiator,kind);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= limit)
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }

}
