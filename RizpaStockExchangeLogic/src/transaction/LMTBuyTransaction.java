package transaction;

import stocks.Stock;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class LMTBuyTransaction extends LMTTransaction {
    public LMTBuyTransaction() {
        super();
    }

    public LMTBuyTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks,User initiator
    ) {
        super(m_Stock, m_Limit, m_NumOfStocks,initiator);
    }

    @Override
    protected List<AllTransactionsKinds> sortAndFilterTransaction(List<AllTransactionsKinds> transactionsToScan, Stock stock, String name) {
        List<AllTransactionsKinds> sortedAndFiltered = transactionsToScan
                .stream().filter(transaction -> transaction.getStock().equals(stock))
                .filter(transaction -> transaction.getPriceOfStock() <= m_Limit)
                .filter(transaction -> !transaction.getInitiator().equals(name))
                .collect(Collectors.toList());

        return sortedAndFiltered;
    }


    @Override
    protected boolean compareTransactionPrice(Transaction transaction) {
        return this.getPriceOfStock() > transaction.getPriceOfStock();
    }

    @Override
    protected User getInitiator() {
        return this.initiator;
    }
}
