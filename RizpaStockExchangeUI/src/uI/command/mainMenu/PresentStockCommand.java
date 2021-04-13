package uI.command.mainMenu;

import uI.command.ICommand;
import uI.IUI;

public class PresentStockCommand implements ICommand {
    IUI UI;

    public PresentStockCommand(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute() { UI.getStockDetails();}
}