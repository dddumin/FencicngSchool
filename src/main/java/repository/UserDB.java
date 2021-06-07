package repository;

import model.User;

import java.sql.*;

public class UserDB implements AutoCloseable{
    private Connection conn;


    public UserDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(User user){
        String sql = "insert into User(login, password, name, date) values(?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            Date regDate = new Date(System.currentTimeMillis());
            preparedStatement.setDate(4, regDate);

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;

            try(ResultSet keys = preparedStatement.getGeneratedKeys()){
                if (keys.next()){
                    user.setId(keys.getInt(1));
                    user.setRegDate(regDate);
                }
            }
            return true;
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return false;
    }

    public boolean delete(User user){
        String sql = "delete from User where User.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public User getById(int id){
        String sql = "select*from User where User.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setLogin(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setName(resultSet.getString(4));
            user.setRegDate(resultSet.getDate(5));
            return user;
        } catch (SQLException ignored) {
        }
        return null;
    }

    public User getByLoginPassword(String login, String password){
        String sql = "select*from User where User.login=? and User.password=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setLogin(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setName(resultSet.getString(4));
            user.setRegDate(resultSet.getDate(5));
            return user;
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return null;
    }

    public boolean update(User user){
        String sql = "update User set User.login=?, User.password=?, User.name=? where User.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setInt(4, user.getId());
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
