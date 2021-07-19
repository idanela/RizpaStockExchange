package accountMovments;

import java.util.ArrayList;
import java.util.List;

public class AccountMovements {
    List<AccountMovement> movements;


    public AccountMovements() {
        movements = new ArrayList<>();
    }

    public synchronized void addMovement(AccountMovement movement)
    {
        this.movements.add(movement);
    }

    public List<AccountMovement> getMovements() {
        return movements;
    }

    public int getVersion()
    {
        return movements.size();
    }
}
