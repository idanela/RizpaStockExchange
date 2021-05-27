package uI.main.singleTransactionTable;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import transaction.Transaction;
import transaction.TransactionMade;
import uI.main.MainController.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import stocks.Stock;
import user.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class singleTransactionTableController implements Initializable {

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    MainController mainController;
    @FXML private TableView<TransactionMade> SingleStockTransactionsMadeTableView;
    @FXML private TableColumn<TransactionMade, String> dateCol;
    @FXML private TableColumn<TransactionMade, String> kindCol;
    @FXML private TableColumn<TransactionMade, Integer> amountCol;
    @FXML private TableColumn<TransactionMade, Integer> priceCol;
    @FXML private TableColumn<TransactionMade, User> initiatorCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("dateOfTransaction"));
        kindCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("transactionKind"));
        amountCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,Integer>("amountOfStocks"));
        priceCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,Integer>("priceOfStockSelledFor"));
        initiatorCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,User>("initiator"));
    }

    public void transactionsUpdated(List<TransactionMade> transactionMade)
    {
        SingleStockTransactionsMadeTableView.getItems().clear();
        SingleStockTransactionsMadeTableView.setItems(setTransactions(transactionMade));
    }

    private ObservableList<TransactionMade> setTransactions(List<TransactionMade> made) {
        ObservableList<TransactionMade> transactionsMade= FXCollections.observableArrayList();
        for (TransactionMade transaction:made) {
            transactionsMade.add(transaction);
        }

        return transactionsMade;
    }

    public void clearTable() {
        SingleStockTransactionsMadeTableView.getItems().clear();
    }

   /* public void transactionPreformed() {
        SingleStockTransactionsMadeTableView.refresh();
        singleTransactionTableController.set
    }*/
}

