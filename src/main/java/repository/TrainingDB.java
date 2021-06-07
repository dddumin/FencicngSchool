package repository;

import model.Apprentice;
import model.Trainer;
import model.Training;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingDB implements AutoCloseable{
    private Connection conn;

    public TrainingDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(Training training){
        String sql = "insert into Training(number_gym, id_trainer, id_apprentice, date) values(?,?,?,?)";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, training.getNumberGym());
            preparedStatement.setInt(2, training.getTrainer().getId());
            preparedStatement.setInt(3, training.getApprentice().getId());
            preparedStatement.setTimestamp(4,  new Timestamp(training.getDate().getTime()));

            int row = preparedStatement.executeUpdate();
            if (row<=0)
                return false;
            try(ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next())
                    training.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean delete(Training training){
        String sql = "delete from Training where Training.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, training.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return false;
    }

    public List<Training> getAllTraining(Trainer trainer){
        String sql = "select*from Training where Training.id_trainer=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setInt(1, trainer.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Training> trainings = new ArrayList<>();
            while (resultSet.next()){
                Training training = new Training();
                training.setId(resultSet.getInt(1));
                training.setNumberGym(resultSet.getInt(2));
                training.setTrainer(trainer);
                training.setApprentice(new ApprenticeDB().getById(resultSet.getInt(4)));
                training.setDate(resultSet.getTimestamp(5));
                trainings.add(training);
            }
            return trainings;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public List<Training> getAllTraining(Apprentice apprentice){
        String sql = "select*from Training where Training.id_apprentice=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setInt(1, apprentice.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Training> trainings = new ArrayList<>();
            while (resultSet.next()){
                Training training = new Training();
                training.setId(resultSet.getInt(1));
                training.setNumberGym(resultSet.getInt(2));
                training.setTrainer(new TrainerDB().getById(resultSet.getInt(3)));
                training.setApprentice(apprentice);
                training.setDate(resultSet.getTimestamp(5));
                trainings.add(training);
            }
            return trainings;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public List<Training> getAllTraining(){
        String sql = "select*from Training";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Training> trainings = new ArrayList<>();
            while (resultSet.next()){
                Training training = new Training();
                training.setId(resultSet.getInt(1));
                training.setNumberGym(resultSet.getInt(2));
                training.setTrainer(new TrainerDB().getById(resultSet.getInt(3)));
                training.setApprentice(new ApprenticeDB().getById(resultSet.getInt(4)));
                training.setDate(resultSet.getTimestamp(5));
                trainings.add(training);
            }
            return trainings;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public List<Training> getTrainingByDate(Date date){
        String sql = "select*from Training where Training.date=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Training> trainings = new ArrayList<>();
            while (resultSet.next()){
                Training training = new Training();
                training.setId(resultSet.getInt(1));
                training.setNumberGym(2);
                training.setTrainer(new TrainerDB().getById(resultSet.getInt(3)));
                training.setApprentice(new ApprenticeDB().getById(resultSet.getInt(4)));
                training.setDate(resultSet.getTimestamp(5));
                trainings.add(training);
            }
            return trainings;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public Training getById(int id){
        String sql = "select*from Training where Training.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Training training = new Training();
            training.setId(resultSet.getInt(1));
            training.setNumberGym(resultSet.getInt(2));
            training.setTrainer(new TrainerDB().getById(resultSet.getInt(3)));
            training.setApprentice(new ApprenticeDB().getById(resultSet.getInt(4)));
            training.setDate(resultSet.getTimestamp(5));
            return training;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }


    @Override
    public void close() throws Exception {
        if (this.conn != null)
            this.conn.close();
    }
}
