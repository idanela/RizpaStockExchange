package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class LMTSellTransaction extends AllTransactionsKinds{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks,User initiator) {
        super(stock,limit,numOfStocks,initiator);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected List<AllTransactionsKinds> sortAndFilterTransaction(List<AllTransactionsKinds> transactionsToScan, Stock stock, String name) {
        List<AllTransactionsKinds>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= m_Limit)
                .filter(transaction -> !transaction.getInitiator().equals(name))
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }

    @Override
    protected User getInitiator() {
        return this.initiator;
    }
}
