package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

public class ExitCommand implements ICommand {
    IUI UI;

    public ExitCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() {
        UI.exit();
    }
}
