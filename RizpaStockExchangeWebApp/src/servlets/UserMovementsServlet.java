package servlets;

import accountMovments.AccountMovement;
import com.google.gson.Gson;
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
import java.util.List;

@WebServlet(name = "UserMovementsServlet", urlPatterns = "/userMovements")
public class UserMovementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    private synchronized void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        String version = req.getParameter("version");
        int movementVersion = Integer.parseInt(version);
        List<AccountMovement> allMovement = user.getMovements().getMovements();
        int currentVersion = user.getMovements().getVersion();
        List<AccountMovement> residualMovements = allMovement.subList(movementVersion,allMovement.size());
        MovementAndVersion mav = new MovementAndVersion(residualMovements,currentVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(mav);
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }


    }

  class MovementAndVersion
  {
      List<AccountMovement> movements;
      int version;

      public MovementAndVersion(List<AccountMovement> movements, int version) {
          this.movements = movements;
          this.version = version;
      }
  }
}
