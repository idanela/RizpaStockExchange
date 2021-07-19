package ui.main;
import ui.ConsoleUI;
import ui.IUI;

public class Program {
    public static void main(String[] args) {
        IUI UI = new ConsoleUI();
        UI.getMainMenu().Run(false);
    }
}
