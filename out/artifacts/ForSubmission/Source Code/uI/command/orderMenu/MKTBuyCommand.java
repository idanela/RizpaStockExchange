package uI.command.orderMenu;

import transaction.MKTBuyTransaction;
import uI.command.ICommand;
import uI.IUI;

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
