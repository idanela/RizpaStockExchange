package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PendingBuyTransaction extends PendingTransaction {

    Stock m_Stock;
    double m_Limit;
    int m_NumOfStocks;
    String m_DateOfTransaction;


    public PendingBuyTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock,limit,numOfStocks);
    }

    @Override
    protected boolean checkTypeToCompare(ITransaction transaction) {
        return transaction instanceof PendingSellTransaction;
    }

    @Override
    protected boolean checkLimit( int limitToCompareTo) {
        return m_Limit >= limitToCompareTo;
    }


}
