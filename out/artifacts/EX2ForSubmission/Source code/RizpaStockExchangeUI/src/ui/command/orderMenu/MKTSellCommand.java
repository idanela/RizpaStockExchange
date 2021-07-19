package ui.command.orderMenu;

import transaction.MKTSellTransaction;
import ui.command.ICommand;
import ui.IUI;

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
