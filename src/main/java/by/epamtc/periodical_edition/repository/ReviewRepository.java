package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Review;

import java.util.List;

public interface ReviewRepository extends BaseRepository<Review> {
    List<Review> findReviewByPeriodicalEditionId(Long periodicalEditionId);
    List<Review> findReviewByUserId(Long userId);
}