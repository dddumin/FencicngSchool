package repository;

import model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerDB implements AutoCloseable {
    private Connection conn;

    public TrainerDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(Trainer trainer) {
        String sql = "insert into Trainer(surname, name, patronymic, experience) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, trainer.getSurname());
            preparedStatement.setString(2, trainer.getName());
            preparedStatement.setString(3, trainer.getPatronymic());
            preparedStatement.setString(4, String.valueOf(trainer.getExperience()));

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    trainer.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public List<Trainer> getTrainers() {
        String sql = "select * from Trainer";
        ArrayList<Trainer> trainers = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(resultSet.getInt(1));
                trainer.setSurname(resultSet.getString(2));
                trainer.setName(resultSet.getString(3));
                trainer.setPatronymic(resultSet.getString(4));
                trainer.setExperience(resultSet.getInt(5));
                trainer.setTrainerSchedule(new TrainerScheduleDB().getByIdTrainer(trainer));
                trainers.add(trainer);
            }
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return trainers;
    }

    public Trainer getById(int id) {
        String sql = "select*from Trainer where Trainer.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Trainer trainer = new Trainer();
            trainer.setId(resultSet.getInt(1));
            trainer.setSurname(resultSet.getString(2));
            trainer.setName(resultSet.getString(3));
            trainer.setPatronymic(resultSet.getString(4));
            trainer.setExperience(resultSet.getInt(5));
            trainer.setTrainerSchedule(new TrainerScheduleDB().getByIdTrainer(trainer));
            return trainer;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public boolean delete(Trainer trainer){
        String sql = "delete from Trainer where Trainer.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, trainer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean update(Trainer trainer){
        String sql = "update Trainer set Trainer.surname=?, Trainer.name=?, Trainer.patronymic=?, Trainer.experience=? where Trainer.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setString(1, trainer.getSurname());
            preparedStatement.setString(2, trainer.getName());
            preparedStatement.setString(3, trainer.getPatronymic());
            preparedStatement.setInt(4, trainer.getExperience());
            preparedStatement.setInt(5, trainer.getId());
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
