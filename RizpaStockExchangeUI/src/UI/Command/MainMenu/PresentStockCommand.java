package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

public class PresentStockCommand implements ICommand {
    IUI UI;

    public PresentStockCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() { UI.getStockDetails();}
}