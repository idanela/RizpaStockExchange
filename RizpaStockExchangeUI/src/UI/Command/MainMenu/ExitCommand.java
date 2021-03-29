package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

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
