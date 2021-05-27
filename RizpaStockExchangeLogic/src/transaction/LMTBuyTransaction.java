package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class LMTBuyTransaction extends LMTTransaction {
    public LMTBuyTransaction() {
        super();
    }

    public LMTBuyTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks, User initiator,String kind) {
        super(m_Stock, m_Limit, m_NumOfStocks,initiator,kind);
    }

    @Override
    protected List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock stock) {
        List<Transaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() <= limit)
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }


    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }
}
