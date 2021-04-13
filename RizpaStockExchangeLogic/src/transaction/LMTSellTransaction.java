package transaction;

import stocks.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class LMTSellTransaction extends AllTransactionsKinds{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock,limit,numOfStocks);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock stock) {
        List<ITransaction>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= m_Limit)
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(ITransaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }

}
