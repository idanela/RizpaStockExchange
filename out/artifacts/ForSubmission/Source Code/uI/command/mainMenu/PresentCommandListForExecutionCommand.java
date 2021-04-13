package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

public class PresentCommandListForExecutionCommand implements ICommand {
    IUI UI;

    public PresentCommandListForExecutionCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.presentCommandListForExecution();
    }
}
