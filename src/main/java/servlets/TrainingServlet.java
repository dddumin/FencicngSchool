package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Apprentice;
import model.ServerError;
import model.Trainer;
import model.Training;
import repository.ApprenticeDB;
import repository.TrainerDB;
import repository.TrainingDB;
import util.GsonExclusionStrategy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@WebServlet("/training")
public class TrainingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String date = req.getParameter("date");
        String numberGym = req.getParameter("number_gym");
        String idTrainer = req.getParameter("id_trainer");
        String idApprentice = req.getParameter("id_apprentice");

        PrintWriter writer = resp.getWriter();
        if (date != null && numberGym != null && idTrainer != null && idApprentice != null){
            try(TrainingDB trainingDB = new TrainingDB();
                TrainerDB trainerDB = new TrainerDB();
                ApprenticeDB apprenticeDB = new ApprenticeDB()) {

                Trainer trainer = trainerDB.getById(Integer.parseInt(idTrainer));
                Apprentice apprentice = apprenticeDB.getById(Integer.parseInt(idApprentice));
                Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(date);
                Training training = new Training(Integer.parseInt(numberGym), trainer, apprentice, date1);
                List<Training> allTraining = trainingDB.getAllTraining();
                long countTrainingByGym = allTraining.stream()
                        .filter(o -> o.getDate().getTime() == date1.getTime() &&
                        o.getNumberGym() == Integer.parseInt(numberGym)).count();
                if (countTrainingByGym < 10){
                    if (trainingDB.add(training))
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(training));
                } else {
                    writer.println(new Gson().toJson(new ServerError("В выбранное время зал занят!!!")));
                    resp.setStatus(400);
                }


            } catch (NumberFormatException e) {
                writer.println(new Gson().toJson(new ServerError("Неверно задан параметр id!")));
                resp.setStatus(400);
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
        String idTrainer = req.getParameter("id_trainer");
        String idApprentice = req.getParameter("id_apprentice");

        PrintWriter writer = resp.getWriter();
        if (id != null || idTrainer != null || idApprentice != null){
            try(TrainingDB trainingDB = new TrainingDB();
                TrainerDB trainerDB = new TrainerDB();
                ApprenticeDB apprenticeDB = new ApprenticeDB()) {

                if (id != null && idTrainer == null && idApprentice == null){
                    Training training = trainingDB.getById(Integer.parseInt(id));
                    if (training != null)
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(training));
                    else {
                        writer.println(new Gson().toJson(new ServerError("Тренировки с заданным id не существует!!!")));
                        resp.setStatus(400);
                    }
                }
                else if (id == null && idTrainer != null && idApprentice == null){
                    List<Training> allTraining = trainingDB.getAllTraining(trainerDB.getById(Integer.parseInt(idTrainer)));
                    if (allTraining != null)
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(allTraining));
                    else {
                        writer.println(new Gson().toJson(new ServerError("Тренера с заданным id не существует!!!")));
                        resp.setStatus(400);
                    }
                }
                else if (id == null && idTrainer == null && idApprentice != null){
                    List<Training> allTraining = trainingDB.getAllTraining(apprenticeDB.getById(Integer.parseInt(idApprentice)));
                    if (allTraining != null)
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(allTraining));
                    else {
                        writer.println(new Gson().toJson(new ServerError("Ученика с заданным id не существует!!!")));
                        resp.setStatus(400);
                    }
                }
            } catch (NumberFormatException ex) {
                writer.println(new Gson().toJson(new ServerError("Неверно задан параметр id!!!")));
                resp.setStatus(400);
            }  catch (SQLException | ClassNotFoundException throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            } catch (Exception e) {
                writer.println(new Gson().toJson(new ServerError(e.getMessage())));
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
            try(TrainingDB trainingDB = new TrainingDB()) {
                Training byId = trainingDB.getById(Integer.parseInt(id));
                if (byId != null){
                    if (trainingDB.delete(byId)){
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byId));
                    } else {
                        writer.write(new Gson().toJson(new ServerError("Удаление тренировки не выполнено!!!")));
                        resp.setStatus(400);
                    }
                } else {
                    writer.write(new Gson().toJson(new ServerError("Тренировки с заданным id не существует")));
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
