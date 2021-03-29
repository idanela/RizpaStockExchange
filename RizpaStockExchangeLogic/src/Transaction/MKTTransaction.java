package Transaction;

import Stocks.Stock;

public class MKTTransaction extends MKTOrLMTTransaction {
    public MKTTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    public MKTTransaction() {
    }

    @Override
    protected boolean checkLimit(int limitToCompareTo) {
        return true;
    }
}
