package uI.main.AllStockTransactionsController;

import holding.Holding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import stockExchangeEngine.StockExchangeEngine;
import transaction.Transaction;
import transaction.TransactionMade;
import uI.main.MainController.MainController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import user.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AllStockTransactionsController implements Initializable {

        MainController mainController;


        @FXML private TableView<Transaction> transactionsMadeTableView;

        @FXML private TableColumn<Transaction, String> transactionsMadeDateCol;
        @FXML private TableColumn<Transaction, String> transactionsMadeKindCol;
        @FXML private TableColumn<Transaction, Integer> transactionsMadeAmountCol;
        @FXML private TableColumn<Transaction, Integer> transactionsMadePriceCol;
        @FXML private TableColumn<Transaction, String> transactionsMadeInitiatorCol;

        @FXML
        private TableView<Transaction> pendingSellTableView;

        @FXML private TableColumn<Transaction, String> pendingSellDateCol;
        @FXML private TableColumn<Transaction, String> pendingSellKindCol;
        @FXML private TableColumn<Transaction, Integer> pendingSellAmountCol;
        @FXML private TableColumn<Transaction, Integer> pendingSellPriceCol;
        @FXML private TableColumn<Transaction, String> pendingSellInitiatorCol;

        @FXML private TableView<Transaction> pendingBuyTableView;

        @FXML private TableColumn<Transaction, String> pendingBuyDateCol;
        @FXML private TableColumn<Transaction, String> pendingBuyKindCol;
        @FXML private TableColumn<Transaction, Integer> pendingBuyAmountCol;
        @FXML private TableColumn<Transaction, Integer> pendingBuyPriceCol;
        @FXML private TableColumn<Transaction,String> pendingBuyInitiatorCol;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                transactionsMadeDateCol.setCellValueFactory(new PropertyValueFactory<Transaction,String>("dateOfTransaction"));
                transactionsMadeKindCol.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactionKind"));
                transactionsMadeAmountCol.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("amountOfStocks"));
                transactionsMadePriceCol.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("priceOfStockSelledFor"));
                transactionsMadeInitiatorCol.setCellValueFactory(new PropertyValueFactory<Transaction,String>("userName"));

                pendingSellDateCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("dateOfTransaction")));
                pendingSellKindCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("transactionKind")));
                pendingSellPriceCol.setCellValueFactory((new PropertyValueFactory<Transaction,Integer>("limit")));
                pendingSellAmountCol.setCellValueFactory((new PropertyValueFactory<Transaction,Integer>("amountOfStocks")));
                pendingSellInitiatorCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("userName")));

                pendingBuyDateCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("dateOfTransaction")));
                pendingBuyKindCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("transactionKind")));
                pendingBuyPriceCol.setCellValueFactory((new PropertyValueFactory<Transaction,Integer>("limit")));
                pendingBuyAmountCol.setCellValueFactory((new PropertyValueFactory<Transaction,Integer>("amountOfStocks")));
                pendingBuyInitiatorCol.setCellValueFactory((new PropertyValueFactory<Transaction,String>("userName")));
        }

         public void setMainController(MainController mainController)
         {
             this.mainController = mainController;
         }

        public void transactionPreformed() {

        }


        private ObservableList<Transaction> getTransactions(List<Transaction> transactions)
        {
                ObservableList<Transaction> transactionCollection= FXCollections.observableArrayList();
                for(Transaction transaction: transactions)
                {
                       transactionCollection.add(transaction);
                }
                return transactionCollection;
        }

        public void presentStockTransactions(String selectedItem) {
                if(selectedItem != null)
                {
                        pendingBuyTableView.refresh();
                        pendingSellTableView.refresh();
                        StockExchangeEngine engine = mainController.getEngine();
                        pendingBuyTableView.setItems(getTransactions(engine.getPendingBuyTransactions().stream().filter(transaction -> transaction.getStock().getStockName().equals(selectedItem)).collect(Collectors.toList())));

                        pendingSellTableView.setItems(getTransactions(engine.getPendingSellTransactions().stream().filter(transaction -> transaction.getStock().getStockName().equals(selectedItem)).collect(Collectors.toList())));
                        transactionsMadeTableView.refresh();
                        transactionsMadeTableView.setItems (getTransactions(engine.getTransactionList().stream().filter(transaction -> transaction.getStock().getStockName().equals(selectedItem)).collect(Collectors.toList())));
                }
                else
                {
                        transactionsMadeTableView.getItems().clear();
                        pendingSellTableView.getItems().clear();
                        pendingBuyTableView.getItems().clear();
                }

        }
}


