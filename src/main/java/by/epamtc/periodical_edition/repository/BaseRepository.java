package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.BaseEntity;

import java.util.List;

public interface BaseRepository<E extends BaseEntity<Long>> {
    E findById(Long id);
    List<E> findAll();
    boolean add(E e);
    boolean update(E e);
    boolean delete(Long id);
}