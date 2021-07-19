package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

public class LoadPreviousSystemStatus implements ICommand {

    IUI UI;

    public LoadPreviousSystemStatus(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute()
    {
        UI.readEngineDataFromFile();
    }
}
