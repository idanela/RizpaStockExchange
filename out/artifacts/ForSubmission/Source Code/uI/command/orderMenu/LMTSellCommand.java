package uI.command.orderMenu;
import transaction.LMTSellTransaction;
import uI.command.ICommand;
import uI.IUI;

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
