package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

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
