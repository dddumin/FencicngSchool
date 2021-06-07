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

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        PrintWriter writer = resp.getWriter();
        if (login != null && password != null){
            try (UserDB userDB = new UserDB()) {
                User byLoginPassword = userDB.getByLoginPassword(login, password);
                if (byLoginPassword == null){
                    writer.println(new Gson().toJson(new ServerError("Неверный логин или пароль!")));
                    resp.setStatus(400);
                } else
                    writer.println(new Gson().toJson(byLoginPassword));
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String id = req.getParameter("id");

        PrintWriter writer = resp.getWriter();
        if (id != null){
            try(UserDB userDB = new UserDB()) {
                User byId = userDB.getById(Integer.parseInt(id));
                if (byId == null){
                    writer.write(new Gson().toJson(new ServerError("Пользователя с заданным id не существует")));
                    resp.setStatus(400);
                } else
                    writer.write(new Gson().toJson(byId));
            } catch (NumberFormatException e){
                writer.write(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(400);
            } catch (Exception throwables) {
                writer.write(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String id = req.getParameter("id");

        PrintWriter writer = resp.getWriter();
        if (id != null){
            try(UserDB userDB = new UserDB()) {
                User byId = userDB.getById(Integer.parseInt(id));
                if (byId != null){
                    if (userDB.delete(byId)){
                        writer.write(new Gson().toJson(byId));
                    } else {
                        writer.write(new Gson().toJson(new ServerError("Удаление пользователя не выполнено!!!")));
                        resp.setStatus(400);
                    }
                } else {
                    writer.write(new Gson().toJson(new ServerError("Пользователя с заданным id не существует")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException e){
                writer.write(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(400);
            } catch (Exception throwables) {
                writer.write(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }
}
