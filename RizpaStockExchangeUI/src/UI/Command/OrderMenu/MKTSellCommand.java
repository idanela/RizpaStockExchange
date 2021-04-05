package UI.Command.OrderMenu;

import Transaction.MKTSellTransaction;
import UI.Command.ICommand;
import UI.IUI;

public class MKTSellCommand implements ICommand {
    IUI UI;

    public MKTSellCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.setTransactionToUse(new MKTSellTransaction());
    }
}
