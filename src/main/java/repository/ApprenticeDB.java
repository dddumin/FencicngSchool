package repository;

import model.Apprentice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApprenticeDB implements AutoCloseable{
    private Connection conn;

    public ApprenticeDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(Apprentice apprentice){
        String sql = "insert into Apprentice(surname, name, patronymic, phone_number) values(?,?,?,?)";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, apprentice.getSurname());
            preparedStatement.setString(2, apprentice.getName());
            preparedStatement.setString(3, apprentice.getPatronymic());
            preparedStatement.setString(4, apprentice.getPhoneNumber());

            int row = preparedStatement.executeUpdate();
            if (row<=0)
                return false;
            try(ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next())
                    apprentice.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public List<Apprentice> getApprentices(){
        String sql = "select * from Apprentice";
        ArrayList<Apprentice> apprentices = new ArrayList<>();
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Apprentice apprentice = new Apprentice();
                apprentice.setId(resultSet.getInt(1));
                apprentice.setSurname(resultSet.getString(2));
                apprentice.setName(resultSet.getString(3));
                apprentice.setPatronymic(resultSet.getString(4));
                apprentice.setPhoneNumber(resultSet.getString(5));
                apprentice.setTrainings(new TrainingDB().getAllTraining(apprentice));
                apprentices.add(apprentice);
            }
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return apprentices;
    }

    public Apprentice getById(int id){
        String sql = "select*from Apprentice where Apprentice.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Apprentice apprentice = new Apprentice();
            apprentice.setId(resultSet.getInt(1));
            apprentice.setSurname(resultSet.getString(2));
            apprentice.setName(resultSet.getString(3));
            apprentice.setPatronymic(resultSet.getString(4));
            apprentice.setPhoneNumber(resultSet.getString(5));
            apprentice.setTrainings(new TrainingDB().getAllTraining(apprentice));
            return apprentice;
        } catch (SQLException | ClassNotFoundException ignored) {
        }
        return null;
    }

    public boolean delete(Apprentice apprentice){
        String sql = "delete from Apprentice where Apprentice.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, apprentice.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean update(Apprentice apprentice){
        String sql = "update Apprentice set Apprentice.surname=?, Apprentice.name=?, Apprentice.patronymic=?, Apprentice.phone_number=? where Apprentice.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setString(1, apprentice.getSurname());
            preparedStatement.setString(2, apprentice.getName());
            preparedStatement.setString(3, apprentice.getPatronymic());
            preparedStatement.setString(4, apprentice.getPhoneNumber());
            preparedStatement.setInt(5, apprentice.getId());
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
