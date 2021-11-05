package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Role;
import by.epamtc.periodical_edition.repository.RoleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {
    private static final String ID_COLUMN = "id";
    private static final String ROLE_NAME_COLUMN = "role_name";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM user_role WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM user_role";
    private static final String INSERT_QUERY = "INSERT INTO user_role (role_name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE user_role SET role_name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM user_role WHERE id = ?";


    private final DataSource dataSource;

    public RoleRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Role findById(Long roleId) {//try with resource
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY)
        ) {
            preparedStatement.setLong(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong(ID_COLUMN));
                role.setRoleName(resultSet.getString(ROLE_NAME_COLUMN));
                return role;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Role();
    }

    @Override
    public List<Role> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            List<Role> roles = new ArrayList<>();
            while(resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong(ID_COLUMN));
                role.setRoleName(resultSet.getString(ROLE_NAME_COLUMN));
                roles.add(role);
            }
            return roles;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean add(Role role) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setString(1, role.getRoleName());
            int effectiveRows = preparedStatement.executeUpdate();
            if(effectiveRows == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    role.setId(resultSet.getLong(ID_COLUMN));
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Role role) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
        ) {
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setLong(2, role.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long roleId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
        ) {
            preparedStatement.setLong(1, roleId);
            //надо метод, который будет удалять ссылки из таблицы user_role_link
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
