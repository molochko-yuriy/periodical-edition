package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Role;

import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import by.epamtc.periodical_edition.repository.RoleRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoleRepositoryImplTest extends BaseRepositoryTest {
    private final RoleRepository roleRepository;
    private final List<Role> roles;

    public RoleRepositoryImplTest() {
        roles = new ArrayList<>();
        roleRepository = new RoleRepositoryImpl(getConnectionPool());
        roles.add(new Role(1L, "admin"));
        roles.add(new Role(2L, "user"));
    }

    @Test
    public void findById_validData_shouldReturnRole() {
        // given
        Role expected = roles.get(0);

        //when
        Role actual = roleRepository.findById(1L);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnRoles() {
        //given && when
        final List<Role> actual = roleRepository.findAll();

        //then
        assertEquals(roles, actual);
    }

    @Test
    public void add_validDate_shouldAddNewRole() {
        //given
        Role expected = new Role(3L, "rider");
        Role actual = new Role(null, "rider");

        //when
        boolean isAdded = roleRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected, actual);
        assertEquals(expected, roleRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_shouldUpdateRole() {
        //given
        Role expected = new Role(2L, "rider");
        Role actual = roleRepository.findById(2L);
        assertEquals(roles.get(1), actual);

        //when
        actual.setRoleName("rider");
        boolean isUpdated = roleRepository.update(expected);

        //then
        Assert.assertTrue(isUpdated);
        assertEquals(expected, actual);
        assertEquals(expected, roleRepository.findById(actual.getId()));
    }

    @Test
    public void delete_validData_shouldDeleteSubscription() {
        //given
        Role expected = roles.get(0);
        Role actual = roleRepository.findById(1L);
        Assert.assertEquals(expected, actual);

        //when
        boolean isDeleted = roleRepository.delete(1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(roleRepository.findById(1L));
    }

    @Test
    public void addRoleToUserById_validData_shouldAddRoleToUserById() {
        //given
        assertEquals(1, roleRepository.findRolesByUserId(2L).size());

        //when
        boolean isAdded = roleRepository.addRoleToUser(2L, 1L);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(2, roleRepository.findRolesByUserId(2L).size());
    }

    @Test
    public void findRolesByUserId_validData_shouldReturnAllUserRoles() {
        //given && when
        List<Role> actual = roleRepository.findRolesByUserId(1L);

        //then
        assertEquals(2, actual.size());
    }

    @Test
    public void deleteRoleFromUser_validData_shouldDeleteRole() {
        //given
        List<Role> roles = roleRepository.findRolesByUserId(1L);
        Assert.assertEquals(2, roles.size());

        //when
        boolean isDeleted = roleRepository.deleteRoleFromUser(1L, 1L);

        //then
        Assert.assertTrue(isDeleted);
        assertEquals(1, roleRepository.findRolesByUserId(1L).size());
    }
}