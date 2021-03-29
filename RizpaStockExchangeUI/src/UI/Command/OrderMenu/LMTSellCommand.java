package UI.Command.OrderMenu;

import Transaction.LMTSellTransaction;
import UI.Command.ICommand;
import UI.IUI;

public class LMTSellCommand implements ICommand {
    IUI UI;


    public LMTSellCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.setTransactionToUse(new LMTSellTransaction());
    }

}
