package transaction;

import stocks.Stock;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FOKSellTransaction extends FOKTransaction{
    public FOKSellTransaction(Stock stock, int limit, int numOfStocks, User initiator, String kind) {
        super(stock,limit,numOfStocks,initiator,kind);
    }

    public FOKSellTransaction() {
    }

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction>sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() >= limit)
                .filter(transaction -> transaction.getAmountOfStocks()>=this.getAmountOfStocks())
                .sorted((t1,t2)->{
                    if(t1.getPriceOfStock() == t2.getPriceOfStock())
                    {
                        return t1.getDateOfTransaction().compareTo(t2.getDateOfTransaction());
                    }
                    return t1.getPriceOfStock()> t2.getPriceOfStock()? 1:-1;
                })
                .collect(Collectors.toList());

        if(!checkIfContainsEnoughStocks(sortedAndFiltered))
        {
            sortedAndFiltered = new ArrayList<>();
        }
        else
        {
            int oldAmount = initiator.getHoldings().get(this.stock.getStockName()).getAmountFreeToUse();
            initiator.getHoldings().get(this.stock.getStockName()).setAmountFreeToUse(oldAmount - this.amountOfStocks);
        }
        return sortedAndFiltered;
    }

    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() < transaction.getPriceOfStock();
    }
}
