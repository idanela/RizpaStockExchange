package ui.command.orderMenu;

import transaction.MKTBuyTransaction;
import ui.command.ICommand;
import ui.IUI;

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
