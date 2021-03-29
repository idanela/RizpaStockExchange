package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

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
