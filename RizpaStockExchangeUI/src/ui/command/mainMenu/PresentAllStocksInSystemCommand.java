package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

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
