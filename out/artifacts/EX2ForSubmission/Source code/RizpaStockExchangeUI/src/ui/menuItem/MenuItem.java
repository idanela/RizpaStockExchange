package ui.menuItem;

import ui.command.ICommand;

public class MenuItem {

    public MenuItem(String text, ICommand command) {
        this.m_Text = text;
        this.m_Command = command;
    }

    String m_Text;
    ICommand m_Command;

    public void selected()
    {
        if(m_Command!=null)
        {
            m_Command.execute();
        }
    }

    public String getText() {
        return m_Text;
    }
}
