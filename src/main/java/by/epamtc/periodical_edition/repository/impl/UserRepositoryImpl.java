package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.entity.User;
import by.epamtc.periodical_edition.repository.UserRepository;

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

    private static final String DELETE_LINK_FROM_USER_ROLE_LINK_QUERY = "DELETE FROM user_role_link WHERE user_id = ?";
    private static final String DELETE_LINK_FROM_REVIEW_QUERY = "DELETE FROM review WHERE user_id = ?";
    private static final String DELETE_LINK_FROM_SUBSCRIPTION_QUERY = "DELETE FROM subscription WHERE user_id = ?";//3

    private static final String SELECT_FROM_SUBSCRIPTION_BY_USER_ID = "SELECT * FROM subscription WHERE user_id = ?";//1 list
    private static final String DELETE_LINK_FROM_CONTENT_QUERY = "DELETE FROM content WHERE subscription_id = ?"; //2

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
                return construct(resultSet);//////////////
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new User();
    }

    private User construct( ResultSet resultSet) throws SQLException {
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

    @Override
    public List<User> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(construct(resultSet));//////////////////////////////
            }
            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public boolean add(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            settingPreparedStatement(preparedStatement, user);
            int effectiveRows = preparedStatement.executeUpdate();
            if (effectiveRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(ID_COLUMN));//////////////////////
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void settingPreparedStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getLastName());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLogin());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getMobilePhone());
        preparedStatement.setInt(7, user.getBalance());
    }

    @Override
    public boolean update(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
        ) {
            settingPreparedStatement(preparedStatement, user);//////////////////////////
            preparedStatement.setLong(8, user.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ) {
            try {
                connection.setAutoCommit(false);
                preparedStatement.setLong(1, userId);
                deleteLinksFromUserRoleLink(connection, userId);
                deleteLinksFromSubscription(connection, userId);
                deleteLinksFromReview(connection, userId);
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void deleteLinksFromUserRoleLink(Connection connection, Long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_USER_ROLE_LINK_QUERY);
        preparedStatement.setLong(1, userId);
        preparedStatement.executeUpdate();
    }

    private void deleteLinksFromReview(Connection connection, Long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_REVIEW_QUERY);
        preparedStatement.setLong(1, userId);
        preparedStatement.executeUpdate();
    }

    private void deleteLinksFromSubscription(Connection connection, Long userId) throws SQLException {
        deleteLinksFromContent(connection, userId);
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_SUBSCRIPTION_QUERY);
        preparedStatement.setLong(1, userId);
        preparedStatement.executeUpdate();
    }

    private void deleteLinksFromContent(Connection connection, Long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_SUBSCRIPTION_BY_USER_ID);
        preparedStatement.setLong(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_CONTENT_QUERY);
            preparedStatement.setLong(1, resultSet.getLong(ID_COLUMN));
            preparedStatement.executeUpdate();
        }
    }
}