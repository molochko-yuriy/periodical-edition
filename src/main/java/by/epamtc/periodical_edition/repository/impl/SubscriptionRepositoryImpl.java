package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.enums.PaymentStatus;
import by.epamtc.periodical_edition.repository.SubscriptionRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private  static final String ID_COLUMN = "id";
    private static final  String PRICE_COLUMN = "price";
    private static final String PAYMENT_STATUS_COLUMN = "payment_status";
    private static final  String USER_ID_COLUMN = "user_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM subscription WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM subscription";
    private static final String INSERT_QUERY = "INSERT INTO subscription (price, payment_status, user_id) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE subscription SET price = ?, payment_status = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM subscription WHERE id =?";
    private static final String DELETE_FROM_CONTENT_QUERY = "DELETE FROM content WHERE subscription_id =?";

    private static final String SELECT_SUBSCRIPTIONS_BY_USER_ID ="SELECT * FROM subscription WHERE user_id = ?" ;

    DataSource dataSource;

    public SubscriptionRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subscription findById(Long subscriptionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ){
            preparedStatement.setLong(1, subscriptionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return construct(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Subscription();
    }

    private  Subscription construct (ResultSet resultSet) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(resultSet.getLong(ID_COLUMN));
        subscription.setPrice(resultSet.getInt(PRICE_COLUMN));
        subscription.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString(PAYMENT_STATUS_COLUMN)));
        subscription.setUserId(resultSet.getLong(USER_ID_COLUMN));
        return subscription;
    }

    @Override
    public List<Subscription> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            List<Subscription> subscriptions = new ArrayList<>();
            while(resultSet.next()){
                subscriptions.add(construct(resultSet));
            }
            return subscriptions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(Subscription subscription) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            settingPreparedStatement(preparedStatement, subscription);
            int effectiveRows = preparedStatement.executeUpdate();
                if(effectiveRows == 1){
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()) {
                        subscription.setId(resultSet.getLong(ID_COLUMN));
                        return true;
                    }
                }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void settingPreparedStatement(PreparedStatement preparedStatement, Subscription subscription) throws SQLException {
        preparedStatement.setInt(1, subscription.getPrice());
        preparedStatement.setString(2, subscription.getPaymentStatus().toString());
        preparedStatement.setLong(3, subscription.getUserId());
    }

    @Override
    public boolean update(Subscription subscription) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)
        ){
            settingPreparedStatement(preparedStatement, subscription);
            preparedStatement.setLong(4, subscription.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long subscriptionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ){
            try {
                connection.setAutoCommit(false);
                preparedStatement.setLong(1,subscriptionId);
                deleteFromContent(connection, subscriptionId);
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

    private void deleteFromContent(Connection connection, Long subscriptionId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_CONTENT_QUERY);
        preparedStatement.setLong(1, subscriptionId);
        preparedStatement.executeUpdate();
    }

    public List<Subscription> findSubscriptionsByUserId(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTIONS_BY_USER_ID);
        ){
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Subscription> subscriptions = new ArrayList<>();
            while(resultSet.next()){
                Subscription subscription = new Subscription();
                subscription.setId(resultSet.getLong(ID_COLUMN));
                subscription.setPrice(resultSet.getInt(PRICE_COLUMN));
                subscription.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString(PAYMENT_STATUS_COLUMN)));
                subscription.setUserId(resultSet.getLong(USER_ID_COLUMN));
                subscriptions.add(subscription);
            }
            return subscriptions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
