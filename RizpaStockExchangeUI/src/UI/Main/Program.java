package UI.Main;

import UI.ConsoleUI;
import UI.IUI;

public class Program {
    public static void main(String[] args) {
        IUI UI = new ConsoleUI();
        UI.getMainMenu().Run(false);
    }
}
