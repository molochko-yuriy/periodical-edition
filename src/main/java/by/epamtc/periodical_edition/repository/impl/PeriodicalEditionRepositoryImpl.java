package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;
import by.epamtc.periodical_edition.enums.PeriodicalEditionType;
import by.epamtc.periodical_edition.enums.Periodicity;
import by.epamtc.periodical_edition.repository.PeriodicalEditionRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodicalEditionRepositoryImpl implements PeriodicalEditionRepository {
    private static final String ID_COLUMN = "id";
    private static final String PERIODICAL_EDITION_TYPE_COLUMN ="periodical_edition_type";
    private static final String PRICE_COLUMN = "price";
    private static final String PERIODICITY_COLUMN = "periodicity";
    private static final String PERIODICAL_EDITION_DESCRIPTION_COLUMN = "periodical_edition_description";
    private static final String TITLE_COLUMN = "title";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM periodical_edition WHERE id = ?";
    private static final String SELECT_ALL_QUERY =  "SELECT * FROM periodical_edition";
    private static final String INSERT_QUERY = "INSERT INTO periodical_edition (" +
            "periodical_edition_type, price, periodicity, periodical_edition_description, title ) VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE periodical_edition SET periodical_edition_type = ?, price = ?, " +
            "periodicity = ?, periodical_edition_description = ?, title = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM periodical_edition WHERE id = ?";
    private static final String DELETE_LINK_FROM_REVIEW_QUERY = "DELETE FROM review WHERE periodical_edition_id = ?";
    private static final String DELETE_LINK_FROM_IMAGE_QUERY = "DELETE FROM periodical_edition_image WHERE periodical_edition_id = ?";
    private static final String DELETE_LINK_FROM_CONTENT_QUERY = "DELETE FROM content WHERE periodical_edition_id = ?";

    private final DataSource dataSource;

    public PeriodicalEditionRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PeriodicalEdition findById(Long periodicalEditionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ){
            preparedStatement.setLong(1, periodicalEditionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return construct(resultSet);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new PeriodicalEdition();
    }

    private PeriodicalEdition construct(ResultSet resultSet) throws SQLException {
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
    public List<PeriodicalEdition> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            List<PeriodicalEdition> periodicalEditions = new ArrayList<>();
            while(resultSet.next()){
                periodicalEditions.add(construct(resultSet));
            }
            return periodicalEditions;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(PeriodicalEdition periodicalEdition) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            settingPreparedStatement(preparedStatement, periodicalEdition);
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    periodicalEdition.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }

            } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void settingPreparedStatement(PreparedStatement preparedStatement, PeriodicalEdition periodicalEdition) throws SQLException {
        preparedStatement.setString(1, periodicalEdition.getPeriodicalEditionType().toString());
        preparedStatement.setInt(2, periodicalEdition.getPrice());
        preparedStatement.setString(3, periodicalEdition.getPeriodicity().toString());
        preparedStatement.setString(4, periodicalEdition.getDescription());
        preparedStatement.setString(5, periodicalEdition.getTitle());
    }

    @Override
    public boolean update(PeriodicalEdition periodicalEdition) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)
        ){
            settingPreparedStatement(preparedStatement, periodicalEdition);
            preparedStatement.setLong(6, periodicalEdition.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long periodicalEditionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ){
            try {
                connection.setAutoCommit(false);
                preparedStatement.setLong(1, periodicalEditionId);
                deleteLinksFromImage(connection, periodicalEditionId);
                deleteLinksFromContent(connection,periodicalEditionId);
                deleteLinksFromReview(connection, periodicalEditionId );
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

     private void deleteLinksFromReview(Connection connection, Long periodicalEditionId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_REVIEW_QUERY);
        preparedStatement.setLong(1, periodicalEditionId);
        preparedStatement.executeUpdate();
    }

    private void deleteLinksFromImage(Connection connection, Long periodicalEditionId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_IMAGE_QUERY);
        preparedStatement.setLong(1, periodicalEditionId);
        preparedStatement.executeUpdate();
    }

    private void deleteLinksFromContent(Connection connection, Long periodicalEditionId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_FROM_CONTENT_QUERY);
        preparedStatement.setLong(1, periodicalEditionId);
        preparedStatement.executeUpdate();
    }
}
