package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LMTBuyTransaction extends MKTOrLMTTransaction {
    public LMTBuyTransaction() {
        super();
    }

    public LMTBuyTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    @Override
    protected boolean checkLimit( ITransaction transaction)
    {
        return m_Limit >= transaction.getPriceOfStock();
    }

}
