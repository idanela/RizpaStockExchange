package Transaction;

import Stocks.Stock;

import java.util.List;

public class LMTSellTransaction extends MKTOrLMTTransaction{

    public LMTSellTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock,limit,numOfStocks);
    }

    public LMTSellTransaction() {
    }

    @Override
    protected boolean checkLimit(int limitToCompareTo) {
        return m_Limit <= limitToCompareTo;
    }
}
