package transaction;

import stocks.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class MKTBuyTransaction extends MKTTransaction{

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .sorted((t1,t2)->{
                    if(t1.getPriceOfStock() == t2.getPriceOfStock())
                    {
                        return t1.getDateOfTransaction().compareTo(t2.getDateOfTransaction());
                    }
                    return t1.getPriceOfStock()< t2.getPriceOfStock()? 1:-1;
                })
                .collect(Collectors.toList());
        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }
}
