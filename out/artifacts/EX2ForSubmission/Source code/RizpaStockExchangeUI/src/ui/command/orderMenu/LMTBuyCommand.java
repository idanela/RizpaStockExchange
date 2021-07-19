package ui.command.orderMenu;

import transaction.LMTBuyTransaction;
import ui.command.ICommand;
import ui.IUI;

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
