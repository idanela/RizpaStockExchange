package ui.main.UsersComponent;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class amountErrorBinding  extends StringBinding {
    Spinner<Integer> amountSpinner;
    RadioButton buy;
    public amountErrorBinding(Spinner<Integer> amountSpinner, RadioButton buyRadioButton) {
        this.amountSpinner = amountSpinner;
        buy = buyRadioButton;
        bind(amountSpinner.getEditor().textProperty());
    }

    @Override
    protected String computeValue() {
        String result="";
        int amount;
        SpinnerValueFactory.IntegerSpinnerValueFactory amountFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) amountSpinner.getValueFactory();
        try
        {
            amount = Integer.parseInt(amountSpinner.getEditor().getText());
        if(!buy.isSelected()) {
            if (amount > amountFactory.getMax() || amount < amountFactory.getMin()) {
                result = "Amount should be a number between " + amountFactory.getMin() + " to " + amountFactory.getMax();
            }
        }
        else
        {
            if(amount < 1)
            result = "Amount should be a positive number";
        }
        }
        catch (Exception e)
        {
            if(!buy.isSelected())
            {
                result ="Amount should be a number between "+amountFactory.getMin()+" to "+amountFactory.getMax();
            }
            else
            result ="Amount should be a positive number";
        }

        return result;
    }
}
