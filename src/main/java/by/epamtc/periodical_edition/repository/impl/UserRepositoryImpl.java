package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.User;
import by.epamtc.periodical_edition.repository.UserRepository;
import com.mysql.cj.jdbc.CallableStatementWrapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final String ID_COLUMN = "id";
    private static final String LAST_NAME_COLUMN = "last_name";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String LOGIN_COLUMN = "login";
    private static final String PASSWORD_COLUMN = "password";
    private static final String EMAIL_COLUMN = "email";
    private static final String MOBILE_PHONE_COLUMN = "mobile_phone";
    private static final String BALANCE_COLUMN = "balance";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users (" +
            "last_name, first_name, login, password, email, mobile_phone, balance) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE users SET last_name = ?, first_name = ?, login = ?, password = ?, " +
            "email = ?,mobile_phone = ?, balance = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findById(Long usersId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ) {
            preparedStatement.setLong(1, usersId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(ID_COLUMN));
                user.setLastName(resultSet.getString(LAST_NAME_COLUMN));
                user.setFirstName(resultSet.getString(FIRST_NAME_COLUMN));
                user.setLogin(resultSet.getString(LOGIN_COLUMN));
                user.setPassword(resultSet.getString(PASSWORD_COLUMN));
                user.setEmail(resultSet.getString(EMAIL_COLUMN));
                user.setMobilePhone(resultSet.getString(MOBILE_PHONE_COLUMN));
                user.setBalance(resultSet.getInt(BALANCE_COLUMN));
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new User();
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(ID_COLUMN));
                user.setFirstName(resultSet.getString(FIRST_NAME_COLUMN));
                user.setLastName(resultSet.getString(LAST_NAME_COLUMN));
                user.setLogin(resultSet.getString(LOGIN_COLUMN));
                user.setPassword(resultSet.getString(PASSWORD_COLUMN));
                user.setEmail(resultSet.getString(EMAIL_COLUMN));
                user.setMobilePhone(resultSet.getString(MOBILE_PHONE_COLUMN));
                user.setBalance(resultSet.getInt(BALANCE_COLUMN));
                users.add(user);
            }
            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public boolean add(User user) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3,user.getLogin());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setString(5,user.getEmail());
            preparedStatement.setString(6, user.getMobilePhone());
            preparedStatement.setInt(7, user.getBalance());
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    user.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
        ){
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3,user.getLogin());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setString(5,user.getEmail());
            preparedStatement.setString(6, user.getMobilePhone());
            preparedStatement.setInt(7, user.getBalance());
            preparedStatement.setLong(8, user.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ){
            preparedStatement.setLong(1, userId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
