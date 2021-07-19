package ui.main.UsersComponent;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class TransactionEnableBinding extends BooleanBinding {

   Spinner<Integer> amountSpinner;
   Spinner<Integer> limitSpinner;

    public TransactionEnableBinding(Spinner<Integer> amountSpinner, Spinner<Integer> limitSpinner) {
        this.amountSpinner = amountSpinner;
        this.limitSpinner = limitSpinner;
        bind(amountSpinner.getEditor().textProperty());
        bind(limitSpinner.getEditor().textProperty());
    }

    @Override
    protected boolean computeValue() {
        boolean isValidValues = true;
        int amountSpinnerValue;
        int limitSpinnerValue;
        try
        {
            amountSpinnerValue = Integer.parseInt(amountSpinner.getEditor().getText());
            limitSpinnerValue = Integer.parseInt(limitSpinner.getEditor().getText());
            SpinnerValueFactory.IntegerSpinnerValueFactory amountFactory =
                    (SpinnerValueFactory.IntegerSpinnerValueFactory) amountSpinner.getValueFactory();
            if(limitSpinnerValue < 1)
                isValidValues = false;
            if(amountSpinnerValue >amountFactory.getMax() || amountSpinnerValue< amountFactory.getMin())
                isValidValues = false;
        }
        catch (Exception e)
        {
            isValidValues = false;
        }

        return !isValidValues;
    }
}
