package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.enums.PaymentStatus;
import by.epamtc.periodical_edition.repository.SubscriptionRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepositoryImpl extends AbstractRepositoryImpl<Subscription> implements SubscriptionRepository {
    private static final String PRICE_COLUMN = "price";
    private static final String PAYMENT_STATUS_COLUMN = "payment_status";
    private static final String USER_ID_COLUMN = "user_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM subscription WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM subscription";
    private static final String INSERT_QUERY = "INSERT INTO subscription (price, payment_status, user_id) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE subscription SET price = ?, payment_status = ?, user_id = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM subscription WHERE id =?";

    private static final String DELETE_FROM_CONTENT_QUERY = "DELETE FROM content WHERE subscription_id =?";
    private static final String SELECT_SUBSCRIPTIONS_BY_USER_ID = "SELECT * FROM subscription WHERE user_id = ?";
    private static final String SELECT_SUBSCRIPTIONS_BY_PERIODICAL_EDITION_ID = "SELECT * FROM subscription s LEFT JOIN " +
            "content c ON s.ID = c.SUBSCRIPTION_ID WHERE PERIODICAL_EDITION_ID = ?";

    public SubscriptionRepositoryImpl(DataSource dataSource) {
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

    @Override
    protected Subscription construct(ResultSet resultSet) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(resultSet.getLong(ID_COLUMN));
        subscription.setPrice(resultSet.getInt(PRICE_COLUMN));
        subscription.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString(PAYMENT_STATUS_COLUMN)));
        subscription.setUserId(resultSet.getLong(USER_ID_COLUMN));
        return subscription;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, Subscription subscription) throws SQLException {
        preparedStatement.setInt(1, subscription.getPrice());
        preparedStatement.setString(2, subscription.getPaymentStatus().toString());
        preparedStatement.setLong(3, subscription.getUserId());
    }

    @Override
    protected void doDeletionOperations(Connection connection, Long id) throws SQLException {
        deleteFromContent(connection, id);
        super.doDeletionOperations(connection, id);
    }

    private void deleteFromContent(Connection connection, Long subscriptionId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_CONTENT_QUERY)) {
            preparedStatement.setLong(1, subscriptionId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(Long userId) {
        return findSubscription(SELECT_SUBSCRIPTIONS_BY_USER_ID, userId);
    }

    @Override
    public List<Subscription> findSubscriptionsThatIncludePeriodicalEditionById(Long periodicalEditionId) {
        return findSubscription(SELECT_SUBSCRIPTIONS_BY_PERIODICAL_EDITION_ID, periodicalEditionId);
    }

    private List<Subscription> findSubscription (String query, Long id){
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Subscription> subscriptions = new ArrayList<>();
                while (resultSet.next()) {
                    subscriptions.add(construct(resultSet));
                }
                return subscriptions;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}