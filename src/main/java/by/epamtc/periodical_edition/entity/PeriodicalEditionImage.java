package by.epamtc.periodical_edition.entity;

public class PeriodicalEditionImage extends BaseEntity<Long> {
    private Long periodicalEditionId;
    private String imagePath;

    public PeriodicalEditionImage() {
    }

    public PeriodicalEditionImage(Long id, Long periodicalEditionId, String imagePath) {
        super.setId(id);
        this.periodicalEditionId = periodicalEditionId;
        this.imagePath = imagePath;
    }

    public Long getPeriodicalEditionId() {
        return periodicalEditionId;
    }

    public void setPeriodicalEditionId(Long periodicalEditionId) {
        this.periodicalEditionId = periodicalEditionId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {return true;}
        if (object == null || getClass() != object.getClass()) {return false;}

        PeriodicalEditionImage aThat = (PeriodicalEditionImage) object;

        if (getId() == null) {if (aThat.getId() != null) {return false;}
        } else if (!getId().equals(aThat.getId())) {return false;}

        if (getImagePath() == null) {
            if (aThat.getImagePath() != null) {return false;}
        } else if (!getImagePath().equals(aThat.getImagePath())) {return false;}

        if (getPeriodicalEditionId() == null) {
            return aThat.getPeriodicalEditionId() == null;
        } else return getPeriodicalEditionId().equals(aThat.getPeriodicalEditionId());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getImagePath() != null ? getImagePath().hashCode() : 0);
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getPeriodicalEditionId() != null ? getPeriodicalEditionId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", periodicalEditionId=" + getPeriodicalEditionId() +
                ", imagePath='" + getImagePath() + '\'' +
                '}';
    }
}