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
    private RoleRepositoryImpl roleRepository;
    private List<Role> roles;

    public RoleRepositoryImplTest() {
        roles = new ArrayList<>();
        roleRepository = new RoleRepositoryImpl(getConnectionPool());
        roles.add(new Role(1L, "admin"));
        roles.add(new Role(2L, "user"));
    }

    @Test
    public void findById_validData_shouldReturnRole() {
        // given && when
        Role expected = roles.get(0);

        //then
        Role actual = roleRepository.findById(1L);
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
    public void delete() {
    }
}