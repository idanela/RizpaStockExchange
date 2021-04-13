package transaction;

import stocks.Stock;
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

        return sortedAndFiltered;
    }


    @Override
    protected boolean compareTransactionPrice(ITransaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }
}
