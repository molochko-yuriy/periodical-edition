package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Review;
import by.epamtc.periodical_edition.entity.User;

import java.util.List;

public interface ReviewRepository {

    Review findById(Long ReviewId);
    List<Review> findAll();
    boolean add(Review review);
    boolean update(Review review);
    boolean delete(Long reviewId);
}
