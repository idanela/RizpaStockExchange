package Transaction;

import Stocks.Stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LMTSellTransaction extends AllTransactionsKinds{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock,limit,numOfStocks);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan) {
        List<ITransaction>sortedAndFiltered = transactionsToScan
                .stream()
                .filter(transaction -> transaction.getPriceOfStock() > m_Limit)
                .collect(Collectors.toList());
        Collections.sort(transactionsToScan, new Comparator<ITransaction>() {
            @Override
            public int compare(ITransaction t1, ITransaction t2) {
                return t1.getPriceOfStock() - t2.getPriceOfStock() ;
            }
        });

        return sortedAndFiltered;
    }

}
