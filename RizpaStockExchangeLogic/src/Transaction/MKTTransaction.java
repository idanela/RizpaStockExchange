package Transaction;

import Stocks.Stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class MKTTransaction extends AllTransactionsKinds {
    public MKTTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    public MKTTransaction() {
    }


    @Override
    protected abstract List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan);

}
