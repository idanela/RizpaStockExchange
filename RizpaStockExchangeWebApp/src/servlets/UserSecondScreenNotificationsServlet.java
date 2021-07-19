package servlets;

import Notification.Notification;
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

@WebServlet(name="UserSecondScreenNotificationsServlet" ,urlPatterns = "/userNotifications")
public class UserSecondScreenNotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String knownVersion = req.getParameter("version");
        int version = Integer.parseInt(knownVersion);
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        List<Notification> notifications = user.getNotifications();
        NotificationsAndVersion noa = new NotificationsAndVersion(notifications, version);
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(noa);
        writer.println(json);
        writer.flush();
    }

    class NotificationsAndVersion
    {
        int version;
        List<Notification> notifications;

        public NotificationsAndVersion(List<Notification> notifications,int version) {
            this.notifications = notifications.subList(version,notifications.size());
            this.version = notifications.size();
        }
    }
}
