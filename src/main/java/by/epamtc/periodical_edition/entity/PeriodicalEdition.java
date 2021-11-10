package by.epamtc.periodical_edition.entity;

import by.epamtc.periodical_edition.enums.PeriodicalEditionType;
import by.epamtc.periodical_edition.enums.Periodicity;

public class PeriodicalEdition extends BaseEntity<Long> {
    private int price;
    private String description;
    private String title;
    private PeriodicalEditionType periodicalEditionType;
    private Periodicity periodicity;

    public PeriodicalEdition() { }

    public PeriodicalEdition(Long id, int price, String description, String title,
                             PeriodicalEditionType periodicalEditionType, Periodicity periodicity) {
        super.setId(id);
        this.price = price;
        this.description = description;
        this.title = title;
        this.periodicalEditionType = periodicalEditionType;
        this.periodicity = periodicity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PeriodicalEditionType getPeriodicalEditionType() {
        return periodicalEditionType;
    }

    public void setPeriodicalEditionType(PeriodicalEditionType periodicalEditionType) {
        this.periodicalEditionType = periodicalEditionType;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }

        PeriodicalEdition aThat = (PeriodicalEdition) object;

        if (getId() == null) {
            if (aThat.getId()  != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getDescription() == null) {
            if (aThat.getDescription()  != null) { return false;}
        } else if (!getDescription().equals(aThat.getDescription())) { return false;}

        if (getTitle() == null) {
            if (aThat.getTitle()  != null) { return false;}
        } else if (!getTitle().equals(aThat.getTitle())) { return false;}

        if (getPeriodicalEditionType() == null) {
            if (aThat.getPeriodicalEditionType()  != null) { return false;}
        } else if (!getPeriodicalEditionType().equals(aThat.getPeriodicalEditionType())) { return false;}

        if (getPeriodicity() == null) {
            if (aThat.getPeriodicity()  != null) { return false;}
        } else if (!getPeriodicity().equals(aThat.getPeriodicity())) { return false;}

        return getPrice() == aThat.getPrice();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = prime * result + (getPeriodicalEditionType() != null ? getPeriodicalEditionType().hashCode() : 0);
        result = prime * result + (getPeriodicity() != null ? getPeriodicity().hashCode() : 0);
        result = prime * result + getPrice();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", periodicalEditionType=" + getPeriodicalEditionType() +
                ", periodicity=" + getPeriodicity() +
                '}';
    }
}