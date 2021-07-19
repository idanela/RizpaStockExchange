package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

public class PreformTransactionCommand implements ICommand
{
    public PreformTransactionCommand(IUI UI) {
        this.UI = UI;
    }

    IUI UI;
    @Override
    public void execute() {
        UI.preformTransaction();
    }
}
