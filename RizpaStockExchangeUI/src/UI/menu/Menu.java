package UI.menu;

import UI.menuItem.MenuItem;
import org.omg.CORBA.IntHolder;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    List<MenuItem> m_MenuItems;
    Scanner m_Scanner;

    public Menu(List<MenuItem> menuItems) {
        this.m_MenuItems = menuItems;
        this.m_Scanner = new Scanner(System.in);
    }


    public void Run(boolean oneTimeOperation)
    {
        boolean userQuit = false;
        IntHolder userSelection = new IntHolder();

        while(!userQuit)
        {
            showMenu();
            try
            {
                userQuit = getUserSelection(userSelection,oneTimeOperation);
                m_MenuItems.get(userSelection.value-1).selected();
                if (userQuit)
                    break;
            }
            catch (InputMismatchException e)
            {
                System.out.println("You've Entered an input which is not an integer.");
            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println("Number Should be between 1-"+ m_MenuItems.size());
            }
        }
    }

    private void showMenu()
    {
        System.out.println("Please pick an option form the menu below(1-" +m_MenuItems.size()+"):");
        int i=1;
        for (MenuItem menuItem: m_MenuItems)
        {
            System.out.println(i+"."+menuItem.getText());
            i++;
        }
    }

    private boolean  getUserSelection(IntHolder userSelection, boolean oneTimeOperation) {
        boolean userQuit = false;
        Scanner scanner = new Scanner(System.in);

        userSelection.value = scanner.nextInt();
        if(userSelection.value < 0 || userSelection.value > m_MenuItems.size())
        {
            throw new IndexOutOfBoundsException();
        }
        else if(userSelection.value == m_MenuItems.size() || oneTimeOperation)
        {
            userQuit = true;
        }

        return userQuit;

    }
}

