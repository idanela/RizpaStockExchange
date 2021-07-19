package servlets;

import holding.Holding;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import user.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name ="CreateNewStockServlet",urlPatterns = "/createNewStock")
public class CreateNewStockServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (this)
        {
            StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
            createStock(req,engine,resp);
        }
    }

    private void createStock(HttpServletRequest req, StockExchangeEngine engine, HttpServletResponse resp) throws IOException {
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        String companyName = req.getParameter("companyName");
        String symbol = req.getParameter("symbol");
        String amount = req.getParameter("amount");
        int numOfStocks = Integer.parseInt(amount);
        String companyWorth = req.getParameter("worth");
        int worth = Integer.parseInt(companyWorth);
        Stock stock = new Stock(symbol,companyName,worth/numOfStocks);
        boolean isStockAdded = engine.addStock(stock);
        if(isStockAdded)
        {
            user.addHolding(new Holding(stock,numOfStocks));
        }
        PrintWriter writer = resp.getWriter();
        writer.print(isStockAdded);
        writer.flush();
    }

}
