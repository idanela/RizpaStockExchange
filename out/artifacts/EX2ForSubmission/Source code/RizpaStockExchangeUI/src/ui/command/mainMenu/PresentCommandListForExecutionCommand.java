package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

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
