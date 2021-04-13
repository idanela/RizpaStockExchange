package uI.command.orderMenu;

import transaction.MKTSellTransaction;
import uI.command.ICommand;
import uI.IUI;

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
