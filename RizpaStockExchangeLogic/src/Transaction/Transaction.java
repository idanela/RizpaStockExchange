package Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction
{
    String m_DateOfTransaction;
    int m_AmountOfStocks;
    double m_PriceOfStockSelledFor;
    double m_TransactionWorth;

    public Transaction(int m_AmountOfStocks, double m_PriceOfStock) {
        this.m_DateOfTransaction = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.m_AmountOfStocks = m_AmountOfStocks;
        this.m_PriceOfStockSelledFor = m_PriceOfStock;
        this.m_TransactionWorth = m_AmountOfStocks* m_PriceOfStock;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "m_Date of transaction:" + m_DateOfTransaction +
                ", Amount of stocks:" + m_AmountOfStocks +
                ", m_PriceOfStock:" + m_PriceOfStockSelledFor +
                ", m_TransactionWorth:" + m_TransactionWorth +
                "}\r\n";
    }

}
