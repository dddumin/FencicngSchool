package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ServerError;
import model.Trainer;
import repository.TrainerDB;
import util.GsonExclusionStrategy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/trainer")
public class TrainerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String patronymic = req.getParameter("patronymic");
        String experience = req.getParameter("experience");

        PrintWriter writer = resp.getWriter();

        if (surname != null && name != null && patronymic != null && experience != null){
            try {
                try(TrainerDB trainerDB = new TrainerDB()){
                    Trainer trainer = new Trainer(surname, name, patronymic, Integer.parseInt(experience));
                    if (trainerDB.add(trainer))
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainer));
                    else{
                        writer.println(new Gson().toJson(new ServerError("Тренер уже существует")));
                        resp.setStatus(400);
                    }
                } catch (NumberFormatException e){
                    writer.println(new Gson().toJson(new ServerError("Неверно задан параметр опыт")));
                    resp.setStatus(400);
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
            try(TrainerDB trainerDB = new TrainerDB()){
                Trainer byId = trainerDB.getById(Integer.parseInt(id));
                if (byId != null)
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byId));
                else {
                    writer.println(new Gson().toJson(new ServerError("Тренера с заданным id не существует")));
                    resp.setStatus(400);
                }
            } catch (Exception e) {
                writer.println(new Gson().toJson(new ServerError(e.getMessage())));
                resp.setStatus(500);
            }
        } else {
            try (TrainerDB trainerDB = new TrainerDB()){
                List<Trainer> trainers = trainerDB.getTrainers();
                writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainers));
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
        String experience = req.getParameter("experience");

        PrintWriter writer = resp.getWriter();

        if (id != null && surname != null && name != null && patronymic != null && experience != null){
            try(TrainerDB trainerDB = new TrainerDB()) {
                Trainer trainer = new Trainer(Integer.parseInt(id), surname, name, patronymic, Integer.parseInt(experience));
                if(trainerDB.update(trainer))
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainer));
                else{
                    writer.println(new Gson().toJson(new ServerError("Ошибка при обновлении Ученика")));
                    resp.setStatus(400);

                }
            } catch (NumberFormatException e ){
                writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(new ServerError("Неверно указан параметр опыт")));
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
            try(TrainerDB trainerDB = new TrainerDB()) {
                Trainer byId = trainerDB.getById(Integer.parseInt(id));
                if(trainerDB.delete(byId)){
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byId));
                } else {
                    writer.println(new Gson().toJson(new ServerError("Тренера с данным id не существует")));
                    resp.setStatus(400);
                }
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }
}
