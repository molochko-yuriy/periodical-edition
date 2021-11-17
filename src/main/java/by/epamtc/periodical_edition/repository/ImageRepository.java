package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.PeriodicalEditionImage;
import java.util.List;


public interface ImageRepository extends BaseRepository<PeriodicalEditionImage> {
    List<PeriodicalEditionImage> findImageByPeriodicalEditionId(Long periodicalEditionId) ;
}