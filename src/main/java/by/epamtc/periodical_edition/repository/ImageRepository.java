package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Image;
import java.util.List;

public interface ImageRepository extends BaseRepository<Image> {
    List<Image> findImageByPeriodicalEditionId(Long periodicalEditionId) ;
}