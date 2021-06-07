package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ServerError;
import model.Trainer;
import model.TrainerSchedule;
import repository.TrainerDB;
import repository.TrainerScheduleDB;
import util.GsonExclusionStrategy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;

@WebServlet("/trainer_schedule")
public class TrainerScheduleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        req.setCharacterEncoding("utf-8");

        String id = req.getParameter("id");
        String mondayStart = req.getParameter("monday_start");
        String mondayFinish = req.getParameter("monday_finish");
        String tuesdayStart = req.getParameter("tuesday_start");
        String tuesdayFinish = req.getParameter("tuesday_finish");
        String wednesdayStart = req.getParameter("wednesday_start");
        String wednesdayFinish = req.getParameter("wednesday_finish");
        String thursdayStart = req.getParameter("thursday_start");
        String thursdayFinish = req.getParameter("thursday_finish");
        String fridayStart = req.getParameter("friday_start");
        String fridayFinish = req.getParameter("friday_finish");
        String saturdayStart = req.getParameter("saturday_start");
        String saturdayFinish = req.getParameter("saturday_finish");
        String sundayStart = req.getParameter("sunday_start");
        String sundayFinish = req.getParameter("sunday_finish");


        PrintWriter writer = resp.getWriter();
        if (id != null) {
            try (TrainerScheduleDB trainerScheduleDB = new TrainerScheduleDB();
                 TrainerDB trainerDB = new TrainerDB()) {

                TrainerSchedule trainerSchedule = new TrainerSchedule();
                trainerSchedule.setTrainer(trainerDB.getById(Integer.parseInt(id)));
                if (mondayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.MONDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(mondayStart).getTime()),
                            mondayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(mondayFinish).getTime()) : null);
                if (tuesdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.TUESDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(tuesdayStart).getTime()),
                            tuesdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(tuesdayFinish).getTime()) : null);
                if (wednesdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.WEDNESDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(wednesdayStart).getTime()),
                            wednesdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(wednesdayFinish).getTime()) : null);
                if (thursdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.THURSDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(thursdayStart).getTime()),
                            thursdayStart != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(thursdayFinish).getTime()) : null);
                if (fridayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.FRIDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(fridayStart).getTime()),
                            fridayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(fridayFinish).getTime()) : null);
                if (saturdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.SATURDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(saturdayStart).getTime()),
                            saturdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(saturdayFinish).getTime()) : null);
                if (sundayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.SUNDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(sundayStart).getTime()),
                            sundayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(sundayFinish).getTime()) : null);
                if (trainerScheduleDB.add(trainerSchedule)) {
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainerSchedule));
                } else {
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(new ServerError("Расписание не добавлено!")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException | ParseException e) {
                writer.println(new Gson().toJson(new ServerError("Неверно заданы параметры")));
                resp.setStatus(500);
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
                throwables.printStackTrace();
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
        if (id != null) {
            try (TrainerScheduleDB trainerScheduleDB = new TrainerScheduleDB();
                 TrainerDB trainerDB = new TrainerDB()) {
                Trainer byId = trainerDB.getById(Integer.parseInt(id));
                if (byId != null) {
                    TrainerSchedule byIdTrainer = trainerScheduleDB.getByIdTrainer(byId);
                    if (byIdTrainer != null) {
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(byIdTrainer));
                    } else {
                        writer.println(new Gson().toJson(new ServerError("Расписания не существует!")));
                        resp.setStatus(400);
                    }
                } else {
                    writer.println(new Gson().toJson(new ServerError("Расписания не существует!")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException e) {
                writer.println(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(500);
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
        String mondayStart = req.getParameter("monday_start");
        String mondayFinish = req.getParameter("monday_finish");
        String tuesdayStart = req.getParameter("tuesday_start");
        String tuesdayFinish = req.getParameter("tuesday_finish");
        String wednesdayStart = req.getParameter("wednesday_start");
        String wednesdayFinish = req.getParameter("wednesday_finish");
        String thursdayStart = req.getParameter("thursday_start");
        String thursdayFinish = req.getParameter("thursday_finish");
        String fridayStart = req.getParameter("friday_start");
        String fridayFinish = req.getParameter("friday_finish");
        String saturdayStart = req.getParameter("saturday_start");
        String saturdayFinish = req.getParameter("saturday_finish");
        String sundayStart = req.getParameter("sunday_start");
        String sundayFinish = req.getParameter("sunday_finish");

        PrintWriter writer = resp.getWriter();
        if (id != null) {
            try (TrainerScheduleDB trainerScheduleDB = new TrainerScheduleDB();
                 TrainerDB trainerDB = new TrainerDB()) {

                TrainerSchedule trainerSchedule = new TrainerSchedule();
                Trainer trainer = trainerDB.getById(Integer.parseInt(id));
                trainerSchedule.setTrainer(trainer);
                if (mondayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.MONDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(mondayStart).getTime()),
                            mondayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(mondayFinish).getTime()) : null);
                if (tuesdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.TUESDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(tuesdayStart).getTime()),
                            tuesdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(tuesdayFinish).getTime()) : null);
                if (wednesdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.WEDNESDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(wednesdayStart).getTime()),
                            wednesdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(wednesdayFinish).getTime()) : null);
                if (thursdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.THURSDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(thursdayStart).getTime()),
                            thursdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(thursdayFinish).getTime()) : null);
                if (fridayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.FRIDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(fridayStart).getTime()),
                            fridayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(fridayFinish).getTime()) : null);
                if (saturdayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.SATURDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(saturdayStart).getTime()),
                            saturdayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(saturdayFinish).getTime()) : null);
                if (sundayStart != null)
                    trainerSchedule.setWorkDay(DayOfWeek.SUNDAY, new Time(new SimpleDateFormat("HH:mm:ss").parse(sundayStart).getTime()),
                            sundayFinish != null ? new Time(new SimpleDateFormat("HH:mm:ss").parse(sundayFinish).getTime()) : null);
                trainer.setTrainerSchedule(trainerSchedule);
                if (trainerScheduleDB.updateByIdTrainer(trainer)) {
                    writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainerSchedule));
                } else {
                    writer.println(new Gson().toJson(new ServerError("Расписание не добавлено!")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException | ParseException e) {
                writer.println(new Gson().toJson(new ServerError("Неверно заданы параметры")));
                resp.setStatus(500);
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
        if (id != null) {
            try (TrainerScheduleDB trainerScheduleDB = new TrainerScheduleDB();
                 TrainerDB trainerDB = new TrainerDB()) {
                Trainer byId = trainerDB.getById(Integer.parseInt(id));
                if (byId != null) {
                    TrainerSchedule trainerSchedule = trainerScheduleDB.getByIdTrainer(byId);
                    if (trainerSchedule != null && trainerScheduleDB.deleteByIdTrainer(byId)) {
                        writer.println(new GsonBuilder().addSerializationExclusionStrategy(new GsonExclusionStrategy()).create().toJson(trainerSchedule));
                    } else {
                        writer.println(new Gson().toJson(new ServerError("Расписания не существует!")));
                        resp.setStatus(400);
                    }
                } else {
                    writer.println(new Gson().toJson(new ServerError("Расписания не существует!")));
                    resp.setStatus(400);
                }
            } catch (NumberFormatException e) {
                writer.println(new Gson().toJson(new ServerError("Неверно задан параметр id")));
                resp.setStatus(500);
            } catch (Exception throwables) {
                writer.println(new Gson().toJson(new ServerError(throwables.getMessage())));
                resp.setStatus(500);
            }
        }
    }
}
