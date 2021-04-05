package Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MKTSellTransaction extends MKTTransaction {

    @Override
    protected List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan) {

        Collections.sort(transactionsToScan, new Comparator<ITransaction>() {
            @Override
            public int compare(ITransaction t1, ITransaction t2) {
                return t1.getPriceOfStock() - t2.getPriceOfStock();
            }
        });
        return transactionsToScan;
    }
}

