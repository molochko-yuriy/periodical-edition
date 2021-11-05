package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Role;

import java.util.List;

public interface RoleRepository {
    Role findById(Long roleId);
    List<Role> findAll();
    boolean add(Role role);
    boolean update(Role role);
    boolean delete(Long roleId);
}