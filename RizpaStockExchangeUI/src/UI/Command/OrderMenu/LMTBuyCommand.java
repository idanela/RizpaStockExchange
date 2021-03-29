package UI.Command.OrderMenu;

import Transaction.LMTBuyTransaction;
import UI.Command.ICommand;
import UI.IUI;

public class LMTBuyCommand implements ICommand {
    IUI UI;

    public LMTBuyCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.setTransactionToUse(new LMTBuyTransaction());
    }
}
