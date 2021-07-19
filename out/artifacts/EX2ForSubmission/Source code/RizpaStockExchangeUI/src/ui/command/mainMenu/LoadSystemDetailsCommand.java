package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

public class LoadSystemDetailsCommand implements ICommand {

    IUI UI;

    public LoadSystemDetailsCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.loadStocksData();
    }
}
