package ui.main.UsersComponent;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.Spinner;

public class limitErrorBinding extends StringBinding {

   Spinner<Integer> limitSpinner;

    public limitErrorBinding(Spinner<Integer> limitSpinner) {
        this.limitSpinner = limitSpinner;
        bind(limitSpinner.getEditor().textProperty());
    }

    @Override
    protected String computeValue() {
        String result ="Limit should be a positive number";
        int limit;
        try
        {
            limit = Integer.parseInt(limitSpinner.getEditor().getText());
            if(limit>=1)
                result ="";

        }
        catch (Exception e)
        {
        }
        return result;
    }
}
