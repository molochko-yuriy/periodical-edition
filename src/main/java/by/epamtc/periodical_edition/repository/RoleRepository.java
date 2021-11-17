package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Role;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role> {
    boolean addRoleToUser(Long userId, Long roleId);
    boolean deleteRoleFromUser(Long userId, Long roleId);
    List<Role> findRolesByUserId(Long userId);
}