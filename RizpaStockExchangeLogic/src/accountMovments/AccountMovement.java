package accountMovments;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountMovement {
    String kind;
    String symbol;
    String date;
    int sum;
    int balanceBefore;
    int balanceAfter;

    public AccountMovement(String kind, String symbol, int balanceBefore, int balanceAfter) {
        this.kind = kind;
        this.symbol = symbol;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.sum = balanceAfter - balanceBefore;
        this.date = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
    }
}
