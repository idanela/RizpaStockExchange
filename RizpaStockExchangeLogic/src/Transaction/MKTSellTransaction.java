package Transaction;

import Stocks.Stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MKTSellTransaction extends MKTTransaction {

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock stock) {

        List<ITransaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .collect(Collectors.toList());
        Collections.sort(sortedAndFiltered, new Comparator<ITransaction>() {
            @Override
            public int compare(ITransaction t1, ITransaction t2) {

                return t1.getPriceOfStock() - t2.getPriceOfStock();
            }
        });
        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(ITransaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }
}

