package servlets;

import com.google.gson.Gson;
import user.User;
import user.UserManager;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "UserListServlet",urlPatterns = {"/usersList"})
public class UserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter())
        {
            Gson gson = new Gson();
            UserManager manager = ServletUtils.getUserManager(getServletContext());
            Set<User> userList = manager.getUsers();
            Set<String> names = new HashSet<>();
            for(User user: userList)
            {
                String role =(user.isAdmin())?"Admin":"Trader";
                names.add(user.getName()+" ("+role+")");
            }

            String json = gson.toJson(names);
            out.println(json);
            out.flush();
        }
    }
}
