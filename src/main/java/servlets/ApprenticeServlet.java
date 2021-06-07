package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Apprentice;
import model.ServerError;
import repository.ApprenticeDB;
import util.GsonExclusionStrategy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/apprentice")
public class ApprenticeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String patronymic = req.getParameter("patronymic");
        String number = req.getParameter("phone_number");

        PrintWriter writer = resp.getWriter();

        if (surname != null && name != null && patronymic != null && number != null){
            try {
                try(ApprenticeDB apprenticeDB = new ApprenticeDB()){
                    Apprentice apprentice = new Apprentice(surname, name, patronymic, number);
                    if (apprenticeDB.add(apprentice))
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(apprentice));
                    else{
                        writer.println(new Gson().toJson(new ServerError("Ученик уже существует")));
                        resp.setStatus(400);
                    }
                }
            } catch (Exception e) {
                writer.println(new Gson().toJson(new ServerError(e.getMessage())));
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
                try(ApprenticeDB apprenticeDB = new ApprenticeDB()){
                    Apprentice byId = apprenticeDB.getById(Integer.parseInt(id));
                    if (byId != null)
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byId));
                    else {
                        writer.println(new Gson().toJson(new ServerError("Ученика с заданным id не существует")));
                        resp.setStatus(400);
                    }
                } catch (Exception e) {
                writer.println(new Gson().toJson(new ServerError(e.getMessage())));
                resp.setStatus(500);
            }
        } else {
            try (ApprenticeDB apprenticeDB = new ApprenticeDB()){
                List<Apprentice> apprentices = apprenticeDB.getApprentices();
                writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(apprentices));
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String id = req.getParameter("id");
        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String patronymic = req.getParameter("patronymic");
        String number = req.getParameter("phone_number");

        PrintWriter writer = resp.getWriter();

        if (id != null && surname != null && name != null && patronymic != null && number != null){
            try(ApprenticeDB apprenticeDB = new ApprenticeDB()) {
                Apprentice apprentice = new Apprentice(Integer.parseInt(id), surname, name, patronymic, number);
                if(apprenticeDB.update(apprentice))
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(apprentice));
                else
                    writer.println(new Gson().toJson(new ServerError("Ошибка при обновлении Ученика")));
            } catch (NumberFormatException e){
                writer.write(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(400);
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
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
            try(ApprenticeDB apprenticeDB = new ApprenticeDB()) {
                Apprentice byId = apprenticeDB.getById(Integer.parseInt(id));
                if (byId != null){
                    if(apprenticeDB.delete(byId)){
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byId));
                    } else {
                        writer.println(new Gson().toJson(new ServerError("Ошибка при удалении")));
                    }
                } else {
                    writer.write(new Gson().toJson(new ServerError("Ученика с заданным id не существует")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException e){
                writer.write(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(400);
            }  catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }
}
