package uI.main.stockExchangeApplication;

import holding.Holding;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uI.main.AllStockTransactionsController.AllStockTransactionsController;
import uI.main.MainController.MainController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import transaction.TransactionMade;
import uI.main.UsersComponent.UserController;
import uI.main.singleTransactionTable.singleTransactionTableController;
import user.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class RizpaStockExchangeController implements Initializable {

        private SimpleBooleanProperty isFileLoaded;
        private SimpleStringProperty selectedFile;
        private StockExchangeEngine engine;
        MainController mainController;

        public RizpaStockExchangeController() {
                isFileLoaded = new SimpleBooleanProperty(false);
                selectedFile = new SimpleStringProperty();
        }
        @FXML private TabPane usersTabPane;
        @FXML private Tab AdminTab;
        @FXML private Button loadXMLButton;
        @FXML private ProgressBar loadXMLProgressBar;
        @FXML private BorderPane transactionsBorderPane;
        @FXML private ComboBox<String> chooseStockTransactionComboBox;
        @FXML private ScrollPane singleStockScrollPane;
        @FXML private ComboBox<String> ChooseSingleStockComboBox;
        @FXML private Label symbolLabel;
        @FXML private Label companyLabel;
        @FXML private Label priceLabel;
        @FXML private Label numberOfTransactionLabel;
        @FXML private Label TransactionsWorthLabel;
        @FXML private Label ErrorLoadingFileLabel;
        @FXML private Label filePathLabel;

        @FXML private TableView <Stock> allStockTableView;
        @FXML private TableColumn<Stock, String> symbolCol;
        @FXML private TableColumn<Stock, String> companyCol;
        @FXML private TableColumn<Stock, Integer> priceCol;
        @FXML private TableColumn<Stock, Integer> numOfTransactionsCol;
        @FXML private TableColumn<Stock, Integer> WorthCol;

        @FXML
        void loadXmlContent(ActionEvent event) {
                ErrorLoadingFileLabel.setVisible(false);
               final AtomicBoolean []hasSameSymbol = new AtomicBoolean[1];
               final   AtomicBoolean[] hasSameCompany = new AtomicBoolean[1];
               final AtomicBoolean []hasSameUser = new AtomicBoolean[1];
               final AtomicBoolean[] userHasInValidStock = new AtomicBoolean[1];
                hasSameCompany[0] = new AtomicBoolean(false);
                hasSameSymbol[0] = new AtomicBoolean(false);
                hasSameUser[0] = new AtomicBoolean(false);
                userHasInValidStock[0] = new AtomicBoolean(false);

                File file = setFileFilters();
                if(file == null)
                        return;
                String absolutePath = file.getAbsolutePath();
                final boolean[] isValidFile = new boolean[1];
                Task<Boolean> task = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                                updateProgress(0,9);
                                isValidFile[0] = engine.getXmlContent(absolutePath,hasSameCompany[0],hasSameSymbol[0],hasSameUser[0],userHasInValidStock[0]);
                                Thread.sleep(400);
                                updateProgress(3,9);
                            Thread.sleep(400);
                            updateProgress(6,9);
                            Thread.sleep(400);
                            updateProgress(9,9);
                                return isValidFile[0];
                        }
                };
                task.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if(task.valueProperty().get())
                    {
                        setLoadedFileUI(absolutePath);
                        try {
                            addUsers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        selectedFile.set("");
                        generateInvalidMessage(absolutePath,hasSameCompany[0],hasSameSymbol[0],hasSameUser[0],userHasInValidStock[0]);
                    }
                });
                BindTaskToUI(task);
               new Thread(task).start();

        }
        void BindTaskToUI(Task<Boolean> task)
        {
                loadXMLProgressBar.progressProperty().bind(task.progressProperty());
        }

        private void setLoadedFileUI(String absolutePath)
        {
                selectedFile.set(absolutePath);
                mainController.setEngine(engine);
                isFileLoaded.set(true);
                populateComboboxes();
                populateAllStockTable();
        }
        private void generateInvalidMessage(String absolutePath, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol, AtomicBoolean hasSameUser, AtomicBoolean userHasInValidStock) {
                ErrorLoadingFileLabel.setVisible(true);
                ErrorLoadingFileLabel.setText(absolutePath+" is not valid because:"+System.lineSeparator());
                if(hasSameCompany.get())
                        ErrorLoadingFileLabel.setText(ErrorLoadingFileLabel.getText()+"There are two stocks with same company"+System.lineSeparator());
                if(hasSameSymbol.get())
                        ErrorLoadingFileLabel.setText(ErrorLoadingFileLabel.getText()+"There are two stock with same name" +System.lineSeparator());
                if (hasSameUser.get())
                        ErrorLoadingFileLabel.setText(ErrorLoadingFileLabel.getText() +"There are two users with same name"+System.lineSeparator());
                if(userHasInValidStock.get())
                        ErrorLoadingFileLabel.setText(ErrorLoadingFileLabel.getText() +"There's a user with stock that is not exists"+System.lineSeparator());
        }

        File setFileFilters()
        {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose File To Load");
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(null);
                return file;
        }
        private void addUsers() throws IOException
        {
                FXMLLoader loader ;
                Map<String, User> users = engine.getUsers();
                usersTabPane.getTabs().remove(1,usersTabPane.getTabs().size());

                for(User user: users.values())
                {
                        Tab tab = new Tab(user.getName());
                        usersTabPane.getTabs().add(tab);
                        loader = new FXMLLoader();
                        URL newUrl = getClass().getResource("../UsersComponent/Users.fxml");
                        loader.setLocation(newUrl);
                        Parent newParent = loader.load(newUrl.openStream());
                        tab.setContent(newParent);
                        UserController userController = loader.getController();
                        userController.setMainController(mainController);
                        userController.setUser(user);
                        mainController.addUserController(userController);
                }
        }

        private void populateAllStockTable()
        {
                symbolCol.setCellValueFactory(new PropertyValueFactory<Stock,String>("stockName"));
                companyCol.setCellValueFactory(new PropertyValueFactory<Stock,String>("stockHolder"));
                priceCol.setCellValueFactory(new PropertyValueFactory<Stock,Integer>("currentPrice"));
                numOfTransactionsCol.setCellValueFactory(new PropertyValueFactory<Stock,Integer>("numOfTransaction"));
                WorthCol.setCellValueFactory(new PropertyValueFactory<Stock,Integer>("transactionsWorth"));
                allStockTableView.setItems(getStocks());
        }

        private ObservableList<Stock> getStocks()
        {
                ObservableList<Stock> stocks= FXCollections.observableArrayList();
                for(Stock stock:engine.getStocks().values())
                stocks.add(stock);
                return stocks;
        }

        private void populateComboboxes()
        {
                ChooseSingleStockComboBox.getItems().clear();
                chooseStockTransactionComboBox.getItems().clear();
                Map<String,Stock> stocks = engine.getStocks();
                stocks.forEach((s, stock) -> ChooseSingleStockComboBox.getItems().add(s));
                stocks.forEach((s, stock) -> chooseStockTransactionComboBox.getItems().add(s));
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                try {
                        usersTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                        {
                                if(newValue != AdminTab)
                                {
                                        chooseStockTransactionComboBox.setValue(null);
                                        ChooseSingleStockComboBox.setValue(null);
                                }
                        });
                       AdminTab.getOnSelectionChanged();
                        populateInitialUI();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                filePathLabel.textProperty().bind(selectedFile);
                chooseStockTransactionComboBox.disableProperty().bind(isFileLoaded.not());
                ChooseSingleStockComboBox.disableProperty().bind(isFileLoaded.not());
        }

        public void setMainController(MainController mainController) {
                this.mainController = mainController;
        }

        void populateInitialUI() throws IOException {
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("Transactions.fxml");
                loader.setLocation(url);
                Parent root = loader.load(url.openStream());
                AllStockTransactionsController controller = loader.getController();
                transactionsBorderPane.setCenter(root);
                loader = new FXMLLoader();
                URL newUrl = getClass().getResource("../singleTransactionTable/Transaction.fxml");
                loader.setLocation(newUrl);
                Parent newParent = loader.load(newUrl.openStream());
                singleTransactionTableController singleTransactionTableController = loader.getController();
                singleStockScrollPane.setContent(newParent);

                mainController = new MainController();
                mainController.setRizpaStockExchangeController(this);
                mainController.setSingleTransactionTableController(singleTransactionTableController);
                mainController.setAllStocksTransactionsController(controller);
        }

        void setEngine(StockExchangeEngine stockExchangeEngine)
        {
                this.engine = stockExchangeEngine;
        }


        @FXML
        void singleStockPicked(ActionEvent event)
        { 
              if(ChooseSingleStockComboBox.getValue()!=null) {
                      Stock stock = engine.getStock(ChooseSingleStockComboBox.getValue());
                      updateStocksDetails(stock);
              }
              else 
              {
                      clearStocksDetails();
              }
        }

        private void clearStocksDetails() {
                symbolLabel.setText("");
                numberOfTransactionLabel.setText("");
                priceLabel.setText("");
                companyLabel.setText("");
                TransactionsWorthLabel.setText("");
                mainController.clearSingleStockTable();
        }

        private void updateStocksDetails(Stock stock) {
                symbolLabel.setText(stock.getStockName());
                numberOfTransactionLabel.setText(String.valueOf(stock.getNumOfTransaction()));
                priceLabel.setText(String.valueOf(stock.getCurrentPrice()));
                companyLabel.setText(stock.getStockHolder());
                TransactionsWorthLabel.setText(String.valueOf((stock.getWorthOfTransactions())));
                List<TransactionMade> transactionMade = stock.getTransactions();
                mainController.populateTransactionMadeTable(transactionMade);
        }


        @FXML
        void allTransactionsComboBoxChanged(ActionEvent event) {
                mainController.presentStockTransaction(chooseStockTransactionComboBox.getSelectionModel().getSelectedItem());

        }

        private void setInitialTable() {
              /*  numOfTransa
                        WorthCol;
                symbolCol.setCellValueFactory(new PropertyValueFactory<Stock,String>("stockName"));
                companyCol.setCellValueFactory(new PropertyValueFactory<Stock,String>("stockHolder"));
                priceCol.setCellValueFactory(new PropertyValueFactory<Stock,Integer>("currentPrice"));
                worthCol.setCellValueFactory(new PropertyValueFactory<Holding,Integer>("totalWorthOfStocks"));
                userStocksTableView.setItems(getHoldings());*/
        }
        /*private ObservableList<Holding> getHoldings()
        {
                ObservableList<Holding> holdings= FXCollections.observableArrayList();
                for(Holding holding: user.getHoldings().values())
                        holdings.add(holding);
                return holdings;
        }
*/
}

