package uI.main;
import uI.ConsoleUI;
import uI.IUI;

public class Program {
    public static void main(String[] args) {
        IUI UI = new ConsoleUI();
        UI.getMainMenu().Run(false);
    }
}
