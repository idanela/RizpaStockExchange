package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

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
