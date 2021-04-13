package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

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
