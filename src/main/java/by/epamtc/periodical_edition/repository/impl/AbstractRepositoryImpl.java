package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.BaseEntity;
import by.epamtc.periodical_edition.repository.BaseRepository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryImpl<E extends BaseEntity<Long>> implements BaseRepository<E> {
    protected static final String ID_COLUMN = "id";
    private final DataSource dataSource;

    public AbstractRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    protected abstract String defineSelectByIdQuery();

    protected abstract String defineSelectAllQuery();

    protected abstract String defineInsertQuery();

    protected abstract String defineUpdateQuery();

    protected abstract String defineDeleteQuery();

    @Override
    public E findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(defineSelectByIdQuery())
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return construct(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected abstract E construct(ResultSet resultSet) throws SQLException;

    @Override
    public List<E> findAll() {
        List<E> entities = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(defineSelectAllQuery());
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                entities.add(construct(resultSet));
            }
            return entities;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entities;
    }

    @Override
    public boolean add(E e) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(defineInsertQuery(), Statement.RETURN_GENERATED_KEYS)
        ) {
            settingPreparedParameter(preparedStatement, e);
            int effectiveRows = preparedStatement.executeUpdate();
            if (effectiveRows == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        e.setId(resultSet.getLong(ID_COLUMN));
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    protected abstract void settingPreparedParameter(PreparedStatement preparedStatement, E e) throws SQLException;

    @Override
    public boolean update(E e) {
        String query = String.format(defineUpdateQuery(), e.getId());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            settingPreparedParameter(preparedStatement, e);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                doDeletionOperations(connection, id);
                connection.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected void doDeletionOperations(Connection connection, Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(defineDeleteQuery())) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
}