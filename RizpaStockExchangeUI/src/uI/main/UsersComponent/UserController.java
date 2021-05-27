package uI.main.UsersComponent;

import holding.Holding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import stocks.Stock;
import transaction.*;
import uI.main.MainController.MainController;
import user.User;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class UserController implements Initializable {

    MainController mainController;
    User user;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setUser(User user) {
        this.user = user;
        setInitialTable();
    }

    public User getUser() {
        return user;
    }

    @FXML private TableView<Holding> userStocksTableView;
    @FXML private TableColumn<Stock, String> symbolCol;
    @FXML private TableColumn<Holding, Integer> amountCol;
    @FXML private TableColumn<Holding, Integer> priceCol;
    @FXML private TableColumn<Holding, Integer> worthCol;

    @FXML private RadioButton buyRadioButton;
    @FXML private ToggleGroup typeOfAction;
    @FXML private RadioButton sellRadioButton;
    @FXML private ComboBox<String> symbolComboBox;
    @FXML private Spinner<Integer> amountSpinner;
    @FXML private RadioButton limitRadioButton;
    @FXML private ToggleGroup strategy;
    @FXML private RadioButton marketRadioButtons;
    @FXML private Button submitActionButton;
    @FXML private Label chooseLimitLabel;
    @FXML private Spinner<Integer> choseLimitSpinner;
    @FXML private Label errorLabel;
    @FXML private Label limitErrorLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submitActionButton.disableProperty().bind((amountSpinner.disabledProperty()));
        amountSpinner.disableProperty().bind(Bindings.and(limitRadioButton.selectedProperty().not(),marketRadioButtons.selectedProperty().not()));
        chooseLimitLabel.visibleProperty().bind(limitRadioButton.selectedProperty());
        choseLimitSpinner.visibleProperty().bind(limitRadioButton.selectedProperty());
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1,10);
        choseLimitSpinner.setValueFactory(factory);
        limitRadioButton.disableProperty().bind(symbolComboBox.valueProperty().isNull());
        marketRadioButtons.disableProperty().bind(symbolComboBox.valueProperty().isNull());
        symbolComboBox.disableProperty().bind(Bindings.and(buyRadioButton.selectedProperty().not(),sellRadioButton.selectedProperty().not()));
        setLimitFocusOut();
        kindOrderSelected(limitRadioButton);
        kindOrderSelected(marketRadioButtons);
    }

    public TableView<Holding> getUserStocksTableView() {
        return userStocksTableView;
    }

    private void kindOrderSelected(RadioButton radioButton) {
        radioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            errorLabel.setText("");
            limitErrorLabel.setText("");
            choseLimitSpinner.getEditor().setText("1");
        }));
    }

    private void setLimitFocusOut() {
        choseLimitSpinner.focusedProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if(!newValue)
                    {
                        try
                        {
                            if(Integer.parseInt(choseLimitSpinner.getEditor().getText())<1)
                            {
                                limitErrorLabel.setText("Limit should be a positive number");
                                choseLimitSpinner.getEditor().setText("1");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            limitErrorLabel.setText("Limit should be a positive number");
                            choseLimitSpinner.getEditor().setText("1");
                        }
                    }
                    else
                        limitErrorLabel.setText("");
                })
        );
    }

    private void setAmountFocusOut() {
        amountSpinner.focusedProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        SpinnerValueFactory.IntegerSpinnerValueFactory Factory =
                                (SpinnerValueFactory.IntegerSpinnerValueFactory) amountSpinner.getValueFactory();
                        try {
                            int num = Integer.valueOf(amountSpinner.getEditor().getText());
                            if (buyRadioButton.isSelected()) {
                                if (num < Factory.getMin())
                                {
                                    errorLabel.setText("Amount should be a positive number");
                                    amountSpinner.getEditor().setText("1");
                                }
                            }
                            else {
                                if (num < Factory.getMin() || num > Factory.getMax())
                                {
                                    errorLabel.setText("Amount should be between " + Factory.getMin() + " to " + Factory.getMax());
                                    amountSpinner.getEditor().setText("1");
                                }

                            }
                        } catch (NumberFormatException e) {
                            errorLabel.setText("Amount should be a positive number");
                            amountSpinner.getEditor().setText("1");
                        }

                    }
                    else errorLabel.setText("");
                })
        );
    }

    private void setInitialTable() {
        symbolCol.setCellValueFactory(new PropertyValueFactory<Stock,String>("stockName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Holding,Integer>("amountOfStocks"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Holding,Integer>("price"));
        worthCol.setCellValueFactory(new PropertyValueFactory<Holding,Integer>("totalWorthOfStocks"));
        userStocksTableView.setItems(getHoldings());
    }
    private ObservableList<Holding> getHoldings()
    {
        ObservableList<Holding> holdings= FXCollections.observableArrayList();
        for(Holding holding: user.getHoldings().values())
            holdings.add(holding);
        return holdings;
    }

    @FXML
    void setBuyDefaultValues(ActionEvent event) {
        amountSpinner.getEditor().setText("1");
        errorLabel.setText("");
        limitErrorLabel.setText("");
        radioButtonChanged();
        Set<String> stocksName =  mainController.getAllSymbols();
        stocksName.forEach(stockName->symbolComboBox.getItems().add(stockName));
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1,10);
        this.amountSpinner.setValueFactory(factory);
        setAmountFocusOut();
    }

    @FXML
    void setSellDefaultValues(ActionEvent event)
    {
        amountSpinner.getEditor().setText("1");
        errorLabel.setText("");
        limitErrorLabel.setText("");
       radioButtonChanged();
        for(Holding holding:user.getHoldings().values())
       {
           symbolComboBox.getItems().add(holding.getStock().getStockName());
       }
    }

    private void radioButtonChanged()
    {
        if(amountSpinner.getValueFactory() != null)
            amountSpinner.getValueFactory().setValue(1);
        marketRadioButtons.setSelected(false);
        limitRadioButton.setSelected(false);
        symbolComboBox.getItems().clear();
    }

    @FXML
    void comboBoxChanged(ActionEvent event)
    {
        if(sellRadioButton.isSelected())
        {
            if(symbolComboBox.getValue()!= null)
            {
                String key = symbolComboBox.getValue();
                Holding holding = user.getHoldings().get(key);
                SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,holding.getAmountOfStocks(),1,10);
                this.amountSpinner.setValueFactory(factory);
                setAmountFocusOut();
            }
        }
    }

    @FXML
    void preformTransaction(ActionEvent event) {
        AllTransactionsKinds transactionToUse;
        int amount = Integer.parseInt(this.amountSpinner.getEditor().getText());
        int limit = 0;
        if(choseLimitSpinner.isVisible())
        {
            limit =Integer.parseInt(choseLimitSpinner.getEditor().getText());
        }
        transactionToUse = getTransactionToUse();
        String stockName = symbolComboBox.getValue();
        boolean isBuyOperation = buyRadioButton.isSelected();
        String kind = marketRadioButtons.isSelected()?"MKT":"LMT";
       List<TransactionMade>transactionsMade = mainController.preformTransaction(transactionToUse,stockName,kind,amount,limit,user,isBuyOperation);
        clearTransactionUI();
        mainController.updatePartnersTables(transactionsMade);
        refreshTable();
        mainController.transactionsPreformed();
    }

    public void refreshTable()
    {
        userStocksTableView.refresh();
        userStocksTableView.setItems(getHoldings());
    }
    private void clearTransactionUI() {
        symbolComboBox.getItems().clear();
        amountSpinner.getEditor().setText("1");
        choseLimitSpinner.getEditor().setText("1");
        marketRadioButtons.setSelected(false);
        limitRadioButton.setSelected(false);
        buyRadioButton.setSelected(false);
        sellRadioButton.setSelected(false);
    }


    private AllTransactionsKinds getTransactionToUse() {
        if(buyRadioButton.isSelected())
        {
            if(marketRadioButtons.isSelected())
            {
                return new MKTBuyTransaction();
            }
            else
            {
                return new LMTBuyTransaction();
            }
        }
        else
        {
            if(marketRadioButtons.isSelected())
            {
                return  new MKTSellTransaction();
            }
            else
            {
                return new LMTSellTransaction();
            }
        }
    }


    private void checkValidTransaction() {
        if(chooseLimitLabel.isVisible())
        {
            checkSpinner(choseLimitSpinner);
        }
        checkSpinner(amountSpinner);
    }

    private void checkSpinner(Spinner<Integer> spinner) {
        int val =  Integer.parseInt(spinner.getEditor().getText());
        SpinnerValueFactory.IntegerSpinnerValueFactory Factory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
            int min = Factory.getMin();
            if(val < min|| val > Factory.getMax())
            {
                setErrorText(spinner);
            }
    }

    private void setErrorText(Spinner<Integer> spinner) {
        spinner.getEditor().setText("1");
        SpinnerValueFactory.IntegerSpinnerValueFactory Factory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
        if(amountSpinner == spinner )
        {
            if(sellRadioButton.isSelected())
                errorLabel.setText(errorLabel.getText()+"Amount should be between "+Factory.getMin()+" to "+Factory.getMax()+System.lineSeparator());
            else
                errorLabel.setText(errorLabel.getText()+"Amount should be a positive number" + System.lineSeparator());
        }
        else
        {
            errorLabel.setText(errorLabel.getText()+"Limit should be a positive number"+System.lineSeparator());
        }
    }
}

