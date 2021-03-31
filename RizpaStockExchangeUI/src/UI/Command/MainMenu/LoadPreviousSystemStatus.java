package UI.Command.MainMenu;

import UI.Command.ICommand;
import UI.IUI;

import java.io.IOException;

public class LoadPreviousSystemStatus implements ICommand {

    IUI UI;

    public LoadPreviousSystemStatus(IUI UI) {
        this.UI = UI;
    }

    @Override
    public void execute()  {
        try {
            UI.readEngineFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}