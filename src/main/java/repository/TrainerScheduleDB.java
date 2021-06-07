package repository;

import model.Trainer;
import model.TrainerSchedule;

import java.sql.*;
import java.time.DayOfWeek;

public class TrainerScheduleDB implements AutoCloseable {
    private Connection conn;

    public TrainerScheduleDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(TrainerSchedule trainerSchedule) {
        String sql = "insert into TrainerSchedule(monday_start, monday_finish, "
                + "tuesday_start, tuesday_finish, "
                + "wednesday_start, wednesday_finish, "
                + "thursday_start, thursday_finish, "
                + "friday_start, friday_finish, "
                + "saturday_start, saturday_finish, "
                + "sunday_start, sunday_finish, id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTime(1, trainerSchedule.getScheduleOfTheDay(DayOfWeek.MONDAY)[0]);
            preparedStatement.setTime(2, trainerSchedule.getScheduleOfTheDay(DayOfWeek.MONDAY)[1]);
            preparedStatement.setTime(3, trainerSchedule.getScheduleOfTheDay(DayOfWeek.TUESDAY)[0]);
            preparedStatement.setTime(4, trainerSchedule.getScheduleOfTheDay(DayOfWeek.TUESDAY)[1]);
            preparedStatement.setTime(5, trainerSchedule.getScheduleOfTheDay(DayOfWeek.WEDNESDAY)[0]);
            preparedStatement.setTime(6, trainerSchedule.getScheduleOfTheDay(DayOfWeek.WEDNESDAY)[1]);
            preparedStatement.setTime(7, trainerSchedule.getScheduleOfTheDay(DayOfWeek.THURSDAY)[0]);
            preparedStatement.setTime(8, trainerSchedule.getScheduleOfTheDay(DayOfWeek.THURSDAY)[1]);
            preparedStatement.setTime(9, trainerSchedule.getScheduleOfTheDay(DayOfWeek.FRIDAY)[0]);
            preparedStatement.setTime(10, trainerSchedule.getScheduleOfTheDay(DayOfWeek.FRIDAY)[1]);
            preparedStatement.setTime(11, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SATURDAY)[0]);
            preparedStatement.setTime(12, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SATURDAY)[1]);
            preparedStatement.setTime(13, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SUNDAY)[0]);
            preparedStatement.setTime(14, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SUNDAY)[1]);
            preparedStatement.setInt(15, trainerSchedule.getTrainer().getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception ignored) {
        }
        return false;
    }

    public TrainerSchedule getByIdTrainer(Trainer trainer) {
        String sql = "select*from TrainerSchedule where TrainerSchedule.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, trainer.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            TrainerSchedule trainerSchedule = new TrainerSchedule();
            trainerSchedule.setWorkDay(DayOfWeek.MONDAY, resultSet.getTime(2), resultSet.getTime(3));
            trainerSchedule.setWorkDay(DayOfWeek.TUESDAY, resultSet.getTime(4), resultSet.getTime(5));
            trainerSchedule.setWorkDay(DayOfWeek.WEDNESDAY, resultSet.getTime(6), resultSet.getTime(7));
            trainerSchedule.setWorkDay(DayOfWeek.THURSDAY, resultSet.getTime(8), resultSet.getTime(9));
            trainerSchedule.setWorkDay(DayOfWeek.FRIDAY, resultSet.getTime(10), resultSet.getTime(11));
            trainerSchedule.setWorkDay(DayOfWeek.SATURDAY, resultSet.getTime(12), resultSet.getTime(13));
            trainerSchedule.setWorkDay(DayOfWeek.SUNDAY, resultSet.getTime(14), resultSet.getTime(15));
            trainerSchedule.setTrainer(trainer);
            return trainerSchedule;
        } catch (SQLException ignored) {
        }
        return null;
    }

    public boolean updateByIdTrainer(Trainer trainer) {
        String sql = "update TrainerSchedule set TrainerSchedule.monday_start=?, TrainerSchedule.monday_finish=?, "
                + "TrainerSchedule.tuesday_start=?, TrainerSchedule.tuesday_finish=?, "
                + "TrainerSchedule.wednesday_start=?, TrainerSchedule.wednesday_finish=?, "
                + "TrainerSchedule.thursday_start=?, TrainerSchedule.thursday_finish=?, "
                + "TrainerSchedule.friday_start=?, TrainerSchedule.friday_finish=?, "
                + "TrainerSchedule.saturday_start=?, TrainerSchedule.saturday_finish=?, "
                + "TrainerSchedule.sunday_start=?, TrainerSchedule.sunday_finish=? where TrainerSchedule.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
            preparedStatement.setTime(1, trainerSchedule.getScheduleOfTheDay(DayOfWeek.MONDAY)[0]);
            preparedStatement.setTime(2, trainerSchedule.getScheduleOfTheDay(DayOfWeek.MONDAY)[1]);
            preparedStatement.setTime(3, trainerSchedule.getScheduleOfTheDay(DayOfWeek.TUESDAY)[0]);
            preparedStatement.setTime(4, trainerSchedule.getScheduleOfTheDay(DayOfWeek.TUESDAY)[1]);
            preparedStatement.setTime(5, trainerSchedule.getScheduleOfTheDay(DayOfWeek.WEDNESDAY)[0]);
            preparedStatement.setTime(6, trainerSchedule.getScheduleOfTheDay(DayOfWeek.WEDNESDAY)[1]);
            preparedStatement.setTime(7, trainerSchedule.getScheduleOfTheDay(DayOfWeek.THURSDAY)[0]);
            preparedStatement.setTime(8, trainerSchedule.getScheduleOfTheDay(DayOfWeek.THURSDAY)[1]);
            preparedStatement.setTime(9, trainerSchedule.getScheduleOfTheDay(DayOfWeek.FRIDAY)[0]);
            preparedStatement.setTime(10, trainerSchedule.getScheduleOfTheDay(DayOfWeek.FRIDAY)[1]);
            preparedStatement.setTime(11, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SATURDAY)[0]);
            preparedStatement.setTime(12, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SATURDAY)[1]);
            preparedStatement.setTime(13, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SUNDAY)[0]);
            preparedStatement.setTime(14, trainerSchedule.getScheduleOfTheDay(DayOfWeek.SUNDAY)[1]);
            preparedStatement.setInt(15, trainer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return false;
    }

    public boolean deleteByIdTrainer(Trainer trainer) {
        String sql = "delete from TrainerSchedule where TrainerSchedule.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, trainer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        if (this.conn != null)
            this.conn.close();
    }


}
