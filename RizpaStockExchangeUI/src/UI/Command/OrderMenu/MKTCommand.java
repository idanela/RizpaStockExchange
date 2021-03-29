package UI.Command.OrderMenu;

import Transaction.MKTTransaction;
import UI.Command.ICommand;
import UI.IUI;

public class MKTCommand implements ICommand {
    IUI UI;

    public MKTCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.setTransactionToUse(new MKTTransaction());
    }
}
