package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Content;
import by.epamtc.periodical_edition.repository.ContentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentRepositoryImpl implements ContentRepository {
    public static final String ID_COLUMN = "id";
    public static final String START_DATE_COLUMN = "start_date";
    public static final String EXPIRATION_DATE_COLUMN = "expiration_date";
    public static final String PRICE_COLUMN = "price";
    public static final String SUBSCRIPTION_ID_COLUMN = "subscription_id";
    public static final String PERIODICAL_EDITION_ID_COLUMN="periodical_edition_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM content WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM content";
    private static final String INSERT_QUERY = "INSERT INTO content (start_date, expiration_date, price," +
            "subscription_id, periodical_edition_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String UPDATE_QUERY = "UPDATE content SET start_date = ?, expiration_date = ?, price = ?," +
            " subscription_id = ?, periodical_edition_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM content WHERE id = ?";



    DataSource dataSource;

    public ContentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Content findById(Long contentId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ){
            preparedStatement.setLong(1, contentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return construct(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Content();
    }

    private Content construct(ResultSet resultSet) throws SQLException {
        Content content = new Content();
        content.setId(resultSet.getLong(ID_COLUMN));
        content.setStartDate(resultSet.getDate(START_DATE_COLUMN).toLocalDate());
        content.setExpirationDate(resultSet.getDate(EXPIRATION_DATE_COLUMN).toLocalDate());
        content.setPrice(resultSet.getInt(PRICE_COLUMN));
        content.setSubscriptionId(resultSet.getLong(SUBSCRIPTION_ID_COLUMN));
        content.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
        return  content;
    }

    @Override
    public List<Content> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            List<Content> contents = new ArrayList<>();
            while(resultSet.next()){
                contents.add(construct(resultSet));
            }
            return contents;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(Content content) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            settingPreparedParameter( preparedStatement,  content);
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows == 1){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    content.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private  void settingPreparedParameter(PreparedStatement preparedStatement, Content content) throws SQLException {
        preparedStatement.setDate(1, Date.valueOf(content.getStartDate()));
        preparedStatement.setDate(2, Date.valueOf(content.getExpirationDate()));
        preparedStatement.setInt(3, content.getPrice());
        preparedStatement.setLong(4, content.getSubscriptionId());
        preparedStatement.setLong(5, content.getPeriodicalEditionId());
    }

    @Override
    public boolean update(Content content) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)
        ){
            settingPreparedParameter( preparedStatement,  content);
            preparedStatement.setLong(6, content.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long contentId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ){
            preparedStatement.setLong(1, contentId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
