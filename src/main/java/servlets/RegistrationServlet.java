package servlets;

import com.google.gson.Gson;
import model.ServerError;
import model.User;
import repository.UserDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        PrintWriter writer = resp.getWriter();

        if (login != null && password != null && name != null){
            try(UserDB userDB = new UserDB()) {
                User user = new User(login, password, name);
                if (userDB.add(user))
                    writer.println(new Gson().toJson(user));
                else {
                    writer.println(new Gson().toJson(new ServerError("Пользователь уже существует")));
                    resp.setStatus(400);
                }
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }
}
