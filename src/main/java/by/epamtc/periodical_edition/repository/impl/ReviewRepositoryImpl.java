package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Review;
import by.epamtc.periodical_edition.repository.ReviewRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepository {
        private static  final String ID_COLUMN = "id";
        private static  final String USER_COMMENT_COLUMN = "user_comment";
        private static  final String RATING_COLUMN = "rating";
        private static  final String USER_ID_COLUMN = "user_id";
        private static  final String PERIODICAL_EDITION_ID_COLUMN = "periodical_edition_id";

        private static final String SELECT_BY_ID_QUERY = "SELECT * FROM review WHERE id = ?";
        private static final String SELECT_ALL_QUERY = "SELECT * FROM review";
        private static final String INSERT_QUERY = "INSERT INTO review (user_comment, rating, user_id," +
                "periodical_edition_id) VALUES (?, ?, ?, ?)";
        private static final String UPDATE_QUERY = "UPDATE review SET user_comment = ?, rating = ?, " +
                "user_id = ?, periodical_edition_id = ? WHERE id = ?";
        private static final String DELETE_QUERY = "DELETE FROM review WHERE id = ?";

        DataSource dataSource;

    public ReviewRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Review findById(Long reviewId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY);
        ){
            preparedStatement.setLong(1, reviewId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return construct(resultSet);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Review();
    }

    private Review construct (ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong(ID_COLUMN));
        review.setUserComment(resultSet.getString(USER_COMMENT_COLUMN));
        review.setRating(resultSet.getInt(RATING_COLUMN));
        review.setUserId(resultSet.getLong(USER_ID_COLUMN));
        review.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
        return review;
    }

    @Override
    public List<Review> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()){
               reviews.add(construct(resultSet));
            }
            return reviews;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(Review review) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            settingPreparedStatement(preparedStatement, review);
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows ==1){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    review.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void settingPreparedStatement(PreparedStatement preparedStatement, Review review) throws SQLException {
        preparedStatement.setString(1, review.getUserComment());
        preparedStatement.setInt(2,review.getRating());
        preparedStatement.setLong(3, review.getUserId());
        preparedStatement.setLong(4, review.getPeriodicalEditionId());
    }

    @Override
    public boolean update(Review review) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)
        ){
            settingPreparedStatement(preparedStatement, review);
            preparedStatement.setLong(5, review.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long reviewId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ){
            preparedStatement.setLong(1, reviewId);
            return  preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
