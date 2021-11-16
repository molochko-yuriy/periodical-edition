package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Review;
import by.epamtc.periodical_edition.repository.ReviewRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryImpl extends AbstractRepositoryImpl<Review> implements ReviewRepository {
    private static final String ID_COLUMN = "id";
    private static final String USER_COMMENT_COLUMN = "user_comment";
    private static final String RATING_COLUMN = "rating";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String PERIODICAL_EDITION_ID_COLUMN = "periodical_edition_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM review WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM review";
    private static final String INSERT_QUERY = "INSERT INTO review (user_comment, rating, user_id," +
            "periodical_edition_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE review SET user_comment = ?, rating = ?, " +
            "user_id = ?, periodical_edition_id = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM review WHERE id = ?";

    private static final String SELECT_REVIEW_BY_PERIODICAL_EDITION_ID = "SELECT * FROM review WHERE periodical_edition_id = ?";
    private static final String SELECT_REVIEW_BY_USER_ID = "SELECT * FROM review WHERE user_id = ?";

    public ReviewRepositoryImpl(DataSource dataSource) {
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
    protected Review construct(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong(ID_COLUMN));
        review.setUserComment(resultSet.getString(USER_COMMENT_COLUMN));
        review.setRating(resultSet.getInt(RATING_COLUMN));
        review.setUserId(resultSet.getLong(USER_ID_COLUMN));
        review.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
        return review;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, Review review) throws SQLException {
        preparedStatement.setString(1, review.getUserComment());
        preparedStatement.setInt(2, review.getRating());
        preparedStatement.setLong(3, review.getUserId());
        preparedStatement.setLong(4, review.getPeriodicalEditionId());
    }

    public List<Review> findReviewByPeriodicalEditionId(Long periodicalEditionId) {
        return findReview(SELECT_REVIEW_BY_PERIODICAL_EDITION_ID, periodicalEditionId);
    }

    public List<Review> findReviewByUserId(Long userId) {
        return findReview(SELECT_REVIEW_BY_USER_ID, userId);
    }

    private List<Review> findReview(String query, Long id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (resultSet.next()) {
                    reviews.add(construct(resultSet));
                }
                return reviews;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


}
