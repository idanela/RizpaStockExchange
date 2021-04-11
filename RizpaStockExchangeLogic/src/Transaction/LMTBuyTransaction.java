package Transaction;

import Stocks.Stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LMTBuyTransaction extends LMTTransaction {
    public LMTBuyTransaction() {
        super();
    }

    public LMTBuyTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock stock) {
        List<ITransaction> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() <= m_Limit)
                .collect(Collectors.toList());
        Collections.sort(sortedAndFiltered,new Comparator<ITransaction>() {
            @Override
            public int compare(ITransaction t1, ITransaction t2) {
                return t2.getPriceOfStock() - t1.getPriceOfStock();
            }
        });

        return sortedAndFiltered;
    }


    @Override
    protected boolean compareTransactionPrice(ITransaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }
}
