package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Content;

import java.util.List;

public interface ContentRepository {
    Content findById(Long contentId);
    List<Content> findAll();
    boolean add(Content  content);
    boolean update(Content content);
    boolean delete(Long contentId);
}
