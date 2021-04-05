package Transaction;

import Stocks.Stock;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LMTTransaction extends AllTransactionsKinds
{
    public LMTTransaction(Stock stock, int limit, int numOfStocks) {
        super(stock, limit, numOfStocks);
    }

    public LMTTransaction() {
    }


    @Override
    protected abstract List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan);
/*   @Override
    protected boolean doTransaction(List<TransactionMade> transactionsMade, List<ITransaction> transactionsToScan, List<ITransaction> toAdd, List<ITransaction> toRemove) {
    boolean isFinished = false;
        for(ITransaction transaction: transactionsMade) {
            if(m_Stock.equals(transaction.getStock())) {
                if(checkLimit(transaction)) {
                    if(m_NumOfStocks >= transaction.getNumOfStocks())
                        toRemove.add(transaction);
                    if (isFinished = preformTransaction(transactionsToScan, transaction ,transactionsMade))
                        break;
                }
            }
        }
        transactionsToScan.removeAll(toRemove);
        if(!isFinished)
        {
            toAdd.add(0,this);
        }

        return isFinished;
    }

    protected abstract boolean checkLimit(ITransaction transaction);*/






}

