package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

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
