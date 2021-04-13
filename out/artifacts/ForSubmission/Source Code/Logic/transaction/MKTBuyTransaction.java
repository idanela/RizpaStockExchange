package transaction;

import stocks.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class MKTBuyTransaction extends MKTTransaction{

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock stock) {
        List<ITransaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .collect(Collectors.toList());
        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(ITransaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }
}
