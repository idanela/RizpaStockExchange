package servlets;

import accountMovments.AccountMovement;
import stockExchangeEngine.StockExchangeEngine;
import user.User;
import utils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name ="BalanceServlet",urlPatterns = "/balance")
public class BalanceServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = SessionUtils.getUsername(req);
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(userName);
        int balance = user.getBalance();
        PrintWriter writer = resp.getWriter();
        writer.print(balance);
    }

    @Override
    protected  synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String amountOfMoney = req.getParameter("amount").trim();
        int amount = Integer.parseInt(amountOfMoney);
        String userName = SessionUtils.getUsername(req);
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(userName);
        int balance = user.getBalance();
        int newBalance= balance + amount;
        user.setBalance(newBalance);
        user.getMovements().addMovement(new AccountMovement("Load money","-",balance,newBalance));
        PrintWriter writer = resp.getWriter();
        writer.print(newBalance);
    }
}