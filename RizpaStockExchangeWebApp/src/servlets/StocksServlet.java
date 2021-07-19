package servlets;


import DTOsToWrite.StockToAdd;
import com.google.gson.Gson;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name ="StocksServlet",urlPatterns ="/stocks" )
public class StocksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      synchronized (getServletContext())
      {
          processRequest(req,resp);
      }
    }

    private void  processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StockExchangeEngine engine =(StockExchangeEngine)getServletContext().getAttribute("engine");
        Map<String, Stock> stocks = engine.getStocks();
        List<StockToAdd> stockList = new ArrayList<>();
        for(Stock stock:stocks.values())
        {
            stockList.add(new StockToAdd(stock));
        }
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(stockList);
        writer.println(json);
        writer.flush();
    }
}
