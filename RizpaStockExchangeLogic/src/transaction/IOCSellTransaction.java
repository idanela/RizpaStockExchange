package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class IOCSellTransaction extends IOCTransaction {
    public IOCSellTransaction(Stock stock, int limit, int numOfStocks, User initiator, String kind) {
        super(stock,limit,numOfStocks,initiator,kind);
    }

    public IOCSellTransaction() {
    }

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= limit)
                .sorted((t1,t2)->{
                    if(t1.getPriceOfStock() == t2.getPriceOfStock())
                    {
                        return t1.getDateOfTransaction().compareTo(t2.getDateOfTransaction());
                    }
                    return t1.getPriceOfStock()> t2.getPriceOfStock()? 1:-1;
                })
                .collect(Collectors.toList());


        int sum = 0;
        for(Transaction transaction:sortedAndFiltered)
        {
            sum += transaction.getAmountOfStocks();
        }

        int oldAmount = initiator.getHoldings().get(this.stock.getStockName()).getAmountFreeToUse();;
        if(sum >= this.amountOfStocks)
        {
            initiator.getHoldings().get(this.stock.getStockName()).setAmountFreeToUse(oldAmount - this.amountOfStocks);
        }
        else
        {
            initiator.getHoldings().get(this.stock.getStockName()).setAmountFreeToUse(oldAmount - sum);
        }
        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }
}
