package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Image;
import by.epamtc.periodical_edition.repository.ImageRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageRepositoryImpl extends AbstractRepositoryImpl<Image> implements ImageRepository {
    private static final String IMAGE_PATH_COLUMN = "image_path";
    private static final String PERIODICAL_EDITION_ID_COLUMN = "periodical_edition_id";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM periodical_edition_image WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM periodical_edition_image";
    private static final String INSERT_QUERY = "INSERT INTO periodical_edition_image ( image_path, " +
            "periodical_edition_id) VALUES ( ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE periodical_edition_image SET  image_path = ?, " +
            "periodical_edition_id = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM periodical_edition_image WHERE id = ?";
    private static final String SELECT_IMAGE_BY_PERIODICAL_EDITION_ID = "SELECT * FROM periodical_edition_image WHERE periodical_edition_id = ?";

    public ImageRepositoryImpl(DataSource datasource) {
        super(datasource);
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
    protected Image construct(ResultSet resultSet) throws SQLException {
        Image image = new Image();
        image.setId(resultSet.getLong(ID_COLUMN));
        image.setImagePath(resultSet.getString(IMAGE_PATH_COLUMN));
        image.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
        return image;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, Image image) throws SQLException {
        preparedStatement.setString(1, image.getImagePath());
        preparedStatement.setLong(2, image.getPeriodicalEditionId());
    }

    public List<Image> findImageByPeriodicalEditionId(Long periodicalEditionId) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IMAGE_BY_PERIODICAL_EDITION_ID)
        ) {
            preparedStatement.setLong(1, periodicalEditionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Image> images = new ArrayList<>();
                while (resultSet.next()) {
                    images.add(construct(resultSet));
                }
                return images;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}