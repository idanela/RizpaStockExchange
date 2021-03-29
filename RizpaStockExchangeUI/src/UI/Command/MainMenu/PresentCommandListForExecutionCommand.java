package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

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
