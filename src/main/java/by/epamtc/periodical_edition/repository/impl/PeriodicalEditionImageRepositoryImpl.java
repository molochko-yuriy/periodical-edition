package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;
import by.epamtc.periodical_edition.entity.PeriodicalEditionImage;
import by.epamtc.periodical_edition.repository.ImageRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodicalEditionImageRepositoryImpl implements ImageRepository {

    private static final String ID_COLUMN = "id";
    private static final String IMAGE_PATH_COLUMN = "image_path";
    private static final String PERIODICAL_EDITION_ID_COLUMN = "periodical_edition_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM periodical_edition_image WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM periodical_edition_image";
    private static  final String INSERT_QUERY = "INSERT INTO periodical_edition_image ( image_path, " +
            "periodical_edition_id) VALUES ( ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE periodical_edition_image SET  image_path = ?, " +
            "periodical_edition_id = ? WHERE id = ?";
    private  static final String DELETE_QUERY = "DELETE FROM periodical_edition_image WHERE id = ?";

    private final DataSource datasource;

    public PeriodicalEditionImageRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public PeriodicalEditionImage findById(Long periodicalEditionImageId) {
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ){
            preparedStatement.setLong(1, periodicalEditionImageId);
            ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               PeriodicalEditionImage periodicalEditionImage = new PeriodicalEditionImage();
               periodicalEditionImage.setId(resultSet.getLong(ID_COLUMN));
               periodicalEditionImage.setImagePath(resultSet.getString(IMAGE_PATH_COLUMN));
               periodicalEditionImage.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
               return periodicalEditionImage;
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new PeriodicalEditionImage();
    }

    @Override
    public List<PeriodicalEditionImage> findAll() {
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            List<PeriodicalEditionImage> periodicalEditionsImages = new ArrayList<>();
            while(resultSet.next()){
                PeriodicalEditionImage periodicalEditionImage = new PeriodicalEditionImage();
                periodicalEditionImage.setId(resultSet.getLong(ID_COLUMN));
                periodicalEditionImage.setImagePath(resultSet.getString(IMAGE_PATH_COLUMN));
                periodicalEditionImage.setPeriodicalEditionId(resultSet.getLong(PERIODICAL_EDITION_ID_COLUMN));
                periodicalEditionsImages.add(periodicalEditionImage);
            }
            return periodicalEditionsImages;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(PeriodicalEditionImage periodicalEditionImage) {
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setString(1, periodicalEditionImage.getImagePath());
            preparedStatement.setLong(2, periodicalEditionImage.getPeriodicalEditionId());
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    periodicalEditionImage.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(PeriodicalEditionImage PeriodicalEditionImage) {
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)
        ){
            preparedStatement.setString(1, PeriodicalEditionImage.getImagePath());
            preparedStatement.setLong(2, PeriodicalEditionImage.getPeriodicalEditionId());
            preparedStatement.setLong(3, PeriodicalEditionImage.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long PeriodicalEditionImageId) {
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
        ){
            preparedStatement.setLong(1, PeriodicalEditionImageId);
            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
