package ui.command.mainMenu;

import ui.command.ICommand;
import ui.IUI;

public class PresentStockCommand implements ICommand {
    IUI UI;

    public PresentStockCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() { UI.getStockDetails();}
}