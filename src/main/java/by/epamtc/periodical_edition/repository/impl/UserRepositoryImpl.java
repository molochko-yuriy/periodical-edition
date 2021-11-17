package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.User;
import by.epamtc.periodical_edition.repository.BaseRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl extends AbstractRepositoryImpl<User> implements BaseRepository<User> {
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
            "email = ?,mobile_phone = ?, balance = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    private static final String DELETE_LINK_FROM_USER_ROLE_LINK_QUERY = "DELETE FROM user_role_link WHERE user_id = ?";
    private static final String DELETE_LINK_FROM_REVIEW_QUERY = "DELETE FROM review WHERE user_id = ?";
    private static final String DELETE_LINK_FROM_SUBSCRIPTION_QUERY = "DELETE FROM subscription WHERE user_id = ?";//3

    private static final String SELECT_FROM_SUBSCRIPTION_BY_USER_ID = "SELECT * FROM subscription WHERE user_id = ?";//1 list
    private static final String DELETE_LINK_FROM_CONTENT_QUERY = "DELETE FROM content WHERE subscription_id = ?"; //2

    public UserRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String defineSelectByIdQuery() {
        return SELECT_BY_ID_QUERY;
    }

    @Override
    protected String defineSelectAllQuery() {
        return SELECT_ALL_QUERY;
    }

    @Override
    protected String defineInsertQuery() {
        return INSERT_QUERY;
    }

    @Override
    protected String defineUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected String defineDeleteQuery() {
        return DELETE_QUERY;
    }


    protected User construct(ResultSet resultSet) throws SQLException {
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
    protected void settingPreparedParameter(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getLastName());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLogin());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getMobilePhone());
        preparedStatement.setInt(7, user.getBalance());
    }

    @Override
    protected void doDeletionOperations(Connection connection, Long id) throws SQLException {
        deleteLinksFromUserRoleLink(connection, id);
        deleteLinksFromReview(connection, id);
        deleteLinksFromSubscription(connection, id);
        super.doDeletionOperations(connection, id);
    }

    private void deleteLinksFromUserRoleLink(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_USER_ROLE_LINK_QUERY)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteLinksFromReview(Connection connection, Long userId) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_REVIEW_QUERY)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteLinksFromSubscription(Connection connection, Long userId) throws SQLException {
        deleteLinksFromContent(connection, userId);
        try(PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_SUBSCRIPTION_QUERY)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteLinksFromContent(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_SUBSCRIPTION_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try (PreparedStatement preparedStatementToDelete = connection.prepareStatement(DELETE_LINK_FROM_CONTENT_QUERY)) {
                        preparedStatementToDelete.setLong(1, resultSet.getLong(ID_COLUMN));
                        preparedStatementToDelete.executeUpdate();
                    }
                }
            }
        }
    }
}