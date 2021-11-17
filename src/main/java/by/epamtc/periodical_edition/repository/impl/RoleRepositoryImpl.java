package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Role;
import by.epamtc.periodical_edition.repository.RoleRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImpl extends AbstractRepositoryImpl<Role> implements RoleRepository {
    private static final String ROLE_NAME_COLUMN = "role_name";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM user_role WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM user_role";
    private static final String INSERT_QUERY = "INSERT INTO user_role (role_name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE user_role SET role_name = ? WHERE id = %d";
    private static final String DELETE_QUERY = "DELETE FROM user_role WHERE id = ?";

    private static final String DELETE_LINK_QUERY = "DELETE FROM user_role_link WHERE role_id = ?";
    private static final String INSERT_ROLE_TO_USER_BY_ID = "INSERT INTO user_role_link (user_id, role_id) VALUES (?, ?)";
    private static final String SELECT_ROLES_BY_USER_ID = "SELECT * FROM user_role_link url LEFT JOIN user_role ur ON url.role_id = ur.id WHERE url.user_id = ?";
    private static final String DELETE_ROLE_FROM_USER_ROLE_LINK = "DELETE FROM user_role_link WHERE user_id = ? AND role_id = ? ";

    public RoleRepositoryImpl(DataSource dataSource) {
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

    protected Role construct(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong(ID_COLUMN));
        role.setRoleName(resultSet.getString(ROLE_NAME_COLUMN));
        return role;
    }

    @Override
    protected void settingPreparedParameter(PreparedStatement preparedStatement, Role role) throws SQLException {
        preparedStatement.setString(1, role.getRoleName());
    }

    @Override
    protected void doDeletionOperations(Connection connection, Long id) throws SQLException {
        deleteRoleLinks(connection, id);
        super.doDeletionOperations(connection, id);
    }

    private void deleteRoleLinks(Connection connection, Long roleId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LINK_QUERY)) {
            preparedStatement.setLong(1, roleId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean addRoleToUser(Long userId, Long roleId) {
        return addRoleToUserOrDeleteRoleFromUser(INSERT_ROLE_TO_USER_BY_ID, userId,roleId);
    }

    @Override
    public boolean deleteRoleFromUser(Long userId, Long roleId) {
        return addRoleToUserOrDeleteRoleFromUser(DELETE_ROLE_FROM_USER_ROLE_LINK, userId, roleId);
    }

    private boolean addRoleToUserOrDeleteRoleFromUser(String query, Long firstId, Long secondId){
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, firstId);
            preparedStatement.setLong(2, secondId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)
        ) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Role> roles = new ArrayList<>();
                while (resultSet.next()) {
                    roles.add(construct(resultSet));
                }
                return roles;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}