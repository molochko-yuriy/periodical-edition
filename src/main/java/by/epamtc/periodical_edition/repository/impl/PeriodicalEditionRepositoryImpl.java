package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;
import by.epamtc.periodical_edition.enums.PeriodicalEditionType;
import by.epamtc.periodical_edition.enums.Periodicity;
import by.epamtc.periodical_edition.repository.PeriodicalEditionRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodicalEditionRepositoryImpl extends AbstractRepositoryImpl<PeriodicalEdition> implements PeriodicalEditionRepository {
    private static final String ID_COLUMN = "id";
    private static final String PERIODICAL_EDITION_TYPE_COLUMN = "periodical_edition_type";
    private static final String PRICE_COLUMN = "price";
    private static final String PERIODICITY_COLUMN = "periodicity";
    private static final String PERIODICAL_EDITION_DESCRIPTION_COLUMN = "periodical_edition_description";
    private static final String TITLE_COLUMN = "title";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM periodical_edition WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM periodical_edition";
    private static final String INSERT_QUERY = "INSERT INTO periodical_edition (" +
            "periodical_edition_type, price, periodicity, periodical_edition_description, title ) VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE periodical_edition SET periodical_edition_type = ?, price = ?, " +
            "periodicity = ?, periodical_edition_description = ?, title = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM periodical_edition WHERE id = ?";

    private static final String DELETE_LINK_FROM_REVIEW_QUERY = "DELETE FROM review WHERE periodical_edition_id = ?";
    private static final String DELETE_LINK_FROM_IMAGE_QUERY = "DELETE FROM periodical_edition_image WHERE periodical_edition_id = ?";
    private static final String DELETE_LINK_FROM_CONTENT_QUERY = "DELETE FROM content WHERE periodical_edition_id = ?";

    private static final String SELECT_PERIODICAL_EDITION_BY_SUBSCRIPTION_ID = "SELECT * FROM periodical_edition pe LEFT JOIN " +
            "content c ON pe.id = c.periodical_edition_id  WHERE SUBSCRIPTION_ID= ?";


    public PeriodicalEditionRepositoryImpl(DataSource dataSource) {
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


    protected PeriodicalEdition construct(ResultSet resultSet) throws SQLException {
        PeriodicalEdition periodicalEdition = new PeriodicalEdition();
        periodicalEdition.setId(resultSet.getLong(ID_COLUMN));
        periodicalEdition.setPeriodicalEditionType(PeriodicalEditionType.valueOf(resultSet.getString(PERIODICAL_EDITION_TYPE_COLUMN)));
        periodicalEdition.setPrice(resultSet.getInt(PRICE_COLUMN));
        periodicalEdition.setPeriodicity(Periodicity.valueOf(resultSet.getString(PERIODICITY_COLUMN)));
        periodicalEdition.setDescription(resultSet.getString(PERIODICAL_EDITION_DESCRIPTION_COLUMN));
        periodicalEdition.setTitle(resultSet.getString(TITLE_COLUMN));
        return periodicalEdition;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, PeriodicalEdition periodicalEdition) throws SQLException {
        preparedStatement.setString(1, periodicalEdition.getPeriodicalEditionType().toString());
        preparedStatement.setInt(2, periodicalEdition.getPrice());
        preparedStatement.setString(3, periodicalEdition.getPeriodicity().toString());
        preparedStatement.setString(4, periodicalEdition.getDescription());
        preparedStatement.setString(5, periodicalEdition.getTitle());
    }

    @Override
    protected void doDeletionOperations(Connection connection, Long id) throws SQLException {
        deleteLinksFromReview(connection, id);
        deleteLinksFromContent(connection, id);
        deleteLinksFromImage(connection, id);
        super.doDeletionOperations(connection, id);
    }

    private void deleteLinksFromReview(Connection connection, Long periodicalEditionId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_REVIEW_QUERY)) {
            preparedStatement.setLong(1, periodicalEditionId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteLinksFromImage(Connection connection, Long periodicalEditionId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_IMAGE_QUERY)) {
            preparedStatement.setLong(1, periodicalEditionId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteLinksFromContent(Connection connection, Long periodicalEditionId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_CONTENT_QUERY)) {
            preparedStatement.setLong(1, periodicalEditionId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<PeriodicalEdition> findPeriodicalEditionsBySubscriptionId(Long subscriptionId) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PERIODICAL_EDITION_BY_SUBSCRIPTION_ID)
        ) {
            preparedStatement.setLong(1, subscriptionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<PeriodicalEdition> periodicalEditions = new ArrayList<>();
                while (resultSet.next()) {
                    periodicalEditions.add(construct(resultSet));
                }
                return periodicalEditions;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
