package Transaction;

import Stocks.Stock;

public class PendingSellTransaction extends PendingTransaction {

    public PendingSellTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock,limit,numOfStocks);
    }

    @Override
    protected boolean checkTypeToCompare(ITransaction transaction) {
        return transaction instanceof PendingBuyTransaction;
    }

    @Override
    protected boolean checkLimit(double limit, double limitToCompareTo) {
        return limit <= limitToCompareTo;
    }
}
