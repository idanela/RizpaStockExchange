package uI.command.orderMenu;

import transaction.LMTBuyTransaction;
import uI.command.ICommand;
import uI.IUI;

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
