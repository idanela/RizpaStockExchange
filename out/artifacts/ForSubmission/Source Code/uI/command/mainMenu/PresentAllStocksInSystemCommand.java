package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

public class PresentAllStocksInSystemCommand implements ICommand {

    IUI UI;

    public PresentAllStocksInSystemCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.presentStocks();
    }
}
