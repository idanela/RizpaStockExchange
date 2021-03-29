package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

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
