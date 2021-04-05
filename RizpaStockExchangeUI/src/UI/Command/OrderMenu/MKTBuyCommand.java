package UI.Command.OrderMenu;

import Transaction.MKTBuyTransaction;
import UI.Command.ICommand;
import UI.IUI;

public class MKTBuyCommand implements ICommand {
    IUI UI;

    public MKTBuyCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.setTransactionToUse(new MKTBuyTransaction());
    }
}
