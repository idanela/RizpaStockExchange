package servlets;

import stockExchangeEngine.StockExchangeEngine;
import user.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@WebServlet(name="LoadStocksAndHoldingServlet", urlPatterns ="/uploadFile" )
@MultipartConfig
public class LoadStocksAndHoldingServlet extends HttpServlet {
    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String userName = SessionUtils.getUsername(req);
        Collection<Part> parts = req.getParts();
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        AtomicBoolean hasSameSymbol = new AtomicBoolean(false);
        AtomicBoolean hasSomeCompany = new AtomicBoolean(false);
        AtomicBoolean hasInvalidStock = new AtomicBoolean(false);
        User user = engine.getUsers().get(userName);
        for(Part part:parts)
        {
            if(part.getSize()!=0)
            if(!engine.getXmlContent(user,part.getInputStream(),hasSomeCompany,hasSameSymbol,hasInvalidStock))
            {
               generateAndSendMessage(hasSameSymbol,hasSomeCompany,hasInvalidStock,resp);
            }
            else
            {
                PrintWriter writer = resp.getWriter();
                writer.println("&nbsp;&nbsp;&nbsp;&nbspData about stocks in file is updated");
            }
        }
    }

    private void generateAndSendMessage(AtomicBoolean hasSameSymbol, AtomicBoolean hasSomeCompany, AtomicBoolean hasInvalidStock, HttpServletResponse resp) throws IOException {

        PrintWriter writer = resp.getWriter();
        if(hasSomeCompany.get())
            writer.println("There are different stocks to same company<br>");
        if(hasSameSymbol.get())
            writer.println("There are two stocks with the same symbol<br>");
        if(hasInvalidStock.get())
            writer.println("There is stock owned by the user that is not defined in file");
    }
}
