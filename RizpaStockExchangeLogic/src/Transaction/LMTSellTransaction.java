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
    protected boolean checkLimit(ITransaction transaction) {
        return m_Limit <= transaction.getPriceOfStock();
    }
}
