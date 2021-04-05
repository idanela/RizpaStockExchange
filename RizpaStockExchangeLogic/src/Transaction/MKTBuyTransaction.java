package Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MKTBuyTransaction extends MKTTransaction{

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan) {
        Collections.sort(transactionsToScan, new Comparator<ITransaction>() {
            @Override
            public int compare(ITransaction t1, ITransaction t2) {
                return  t2.getPriceOfStock() - t1.getPriceOfStock();
            }
        });
        return transactionsToScan;
    }
}
