package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class MKTSellTransaction extends MKTTransaction {

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock, String name) {
        List<Transaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
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

