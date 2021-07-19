package ui.command.orderMenu;
import transaction.LMTSellTransaction;
import ui.command.ICommand;
import ui.IUI;

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
