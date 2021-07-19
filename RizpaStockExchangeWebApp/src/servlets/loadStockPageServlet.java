package servlets;

import com.google.gson.Gson;
import holding.Holding;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import DTOsToWrite.StockToAdd;
import user.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "loadStockPageServlet", urlPatterns = "/loadStock")
public class loadStockPageServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        String symbol = req.getParameter("symbol");
        Holding holding = user.getHoldings().get(symbol);
        int amount = 0;
        int amountFreeToUse = 0;
        if(!user.isAdmin())
        {
            if(holding!=null)
            {
                amount = holding.getAmountOfStocks();
                amountFreeToUse =holding.getAmountFreeToUse();
            }
        }
        Stock stock = engine.getStock(symbol);
        StockAndAmount saa = new StockAndAmount(stock,amount,amountFreeToUse);
        Gson gson = new Gson();
        PrintWriter writer = resp.getWriter();
        String json = gson.toJson(saa);
        writer.println(json);
        writer.flush();
    }

    class StockAndAmount
    {
        StockToAdd stock;
        int amount;
        int amountFreeToUse;

        public StockAndAmount(Stock stock, int amount, int amountFreeToUse) {
            this.stock = new StockToAdd(stock);
            this.amount = amount;
            this.amountFreeToUse =amountFreeToUse;
        }
    }
}
