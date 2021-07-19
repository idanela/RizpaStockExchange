package DTOsToWrite;

import transaction.Transaction;
import user.User;

public class TransactionToAdd {
    String kind;
    String instruction;
    String date;
    String symbol;
    int amount;
    int price;
    public TransactionToAdd(Transaction transaction, User user) {
     this.kind = transaction.getTransactionKind();
     this.symbol = transaction.getStock().getStockName();
     this.instruction = transaction.getBuyer().equals(user)?"Buy":"Sell";
     this.date = transaction.getDateOfTransaction();
     this.price = transaction.getPriceOfStock();
     this.amount = transaction.getAmountOfStocks();
    }
}
