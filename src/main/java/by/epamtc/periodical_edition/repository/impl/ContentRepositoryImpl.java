package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Content;
import by.epamtc.periodical_edition.repository.ContentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentRepositoryImpl extends AbstractRepositoryImpl<Content> implements ContentRepository {
    public static final String ID_COLUMN = "id";
    public static final String START_DATE_COLUMN = "start_date";
    public static final String EXPIRATION_DATE_COLUMN = "expiration_date";
    public static final String PRICE_COLUMN = "price";
    public static final String SUBSCRIPTION_ID_COLUMN = "subscription_id";
    public static final String PERIODICAL_EDITION_ID_COLUMN = "periodical_edition_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM content WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM content";
    private static final String INSERT_QUERY = "INSERT INTO content (start_date, expiration_date, price," +
            "subscription_id, periodical_edition_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String UPDATE_QUERY = "UPDATE content SET start_date = ?, expiration_date = ?, price = ?," +
            " subscription_id = ?, periodical_edition_id = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM content WHERE id = ?";

    private static final String SELECT_CONTENT_BY_SUBSCRIPTION_ID = "SELECT * FROM content WHERE subscription_id = ?";
    private static final String SELECT_CONTENT_BY_PERIODICAL_EDITION_ID = "SELECT * FROM content WHERE periodical_edition_id = ?";
    private static final String DELETE_FROM_SUBSCRIPTION_PERIODICAL_EDITION = "DELETE FROM content WHERE subscription_id = ? AND periodical_edition_id = ? ";

    public ContentRepositoryImpl(DataSource dataSource) {
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
    protected Content construct(ResultSet resultSet) throws SQLException {
        Content content = new Content();
        content.setId(resultSet.getLong(ID_COLUMN));
        content.setStartDate(resultSet.getDate(START_DATE_COLUMN).toLocalDate());
        content.setExpirationDate(resultSet.getDate(EXPIRATION_DATE_COLUMN).toLocalDate());
        content.setPrice(resultSet.getInt(PRICE_COLUMN));
        content.setSubscriptionId(resultSet.getLong(SUBSCRIPTION_ID_COLUMN));
        content.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
        return content;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, Content content) throws SQLException {
        preparedStatement.setDate(1, Date.valueOf(content.getStartDate()));
        preparedStatement.setDate(2, Date.valueOf(content.getExpirationDate()));
        preparedStatement.setInt(3, content.getPrice());
        preparedStatement.setLong(4, content.getSubscriptionId());
        preparedStatement.setLong(5, content.getPeriodicalEditionId());
    }

    public List<Content> findContentBySubscriptionId(Long subscriptionId) {
        return findContent(SELECT_CONTENT_BY_SUBSCRIPTION_ID, subscriptionId);
    }

    public List<Content> findContentByPeriodicalEditionId(Long periodicalEditionId) {
        return findContent(SELECT_CONTENT_BY_PERIODICAL_EDITION_ID, periodicalEditionId);
    }

    private List<Content> findContent(String query, Long id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Content> contents = new ArrayList<>();
                while (resultSet.next()) {
                    contents.add(construct(resultSet));
                }
                return contents;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deletePeriodicalEditionFromSubscription(Long subscriptionId, Long periodicalEditionId) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_SUBSCRIPTION_PERIODICAL_EDITION)
        ) {
            preparedStatement.setLong(1, subscriptionId);
            preparedStatement.setLong(2, periodicalEditionId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


}
