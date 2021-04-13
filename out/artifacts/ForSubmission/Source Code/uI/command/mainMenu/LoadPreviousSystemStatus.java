package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

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
