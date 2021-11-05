package by.epamtc.periodical_edition.entity;

import java.time.LocalDate;

public class Content {
    private Long id;
    private LocalDate startDate;
    private LocalDate expirationDate;
    private int price;
    private Long subscriptionId;
    private Long periodicalEditionId;

    public Content(){}

    public Content(Long id, LocalDate startDate, LocalDate expirationDate, int price,
                   Long subscriptionId, Long periodicalEditionId) {
        this.id = id;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.price = price;
        this.subscriptionId = subscriptionId;
        this.periodicalEditionId = periodicalEditionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getPeriodicalEditionId() {
        return periodicalEditionId;
    }

    public void setPeriodicalEditionId(Long periodicalEditionId) {
        this.periodicalEditionId = periodicalEditionId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {return true;}
        if (object == null || getClass() != object.getClass()) { return false; }
        Content aThat = (Content) object;

        if (getId() == null) {
            if (aThat.getId() != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getPeriodicalEditionId() == null) {
            if (aThat.getPeriodicalEditionId() != null) { return false;}
        } else if (!getPeriodicalEditionId().equals(aThat.getPeriodicalEditionId())) { return false;}

        if (getStartDate() == null) {
            if (aThat.getStartDate() != null) { return false;}
        } else if (!getStartDate().equals(aThat.getStartDate())) { return false;}

        if (getExpirationDate() == null) {
            if (aThat.getExpirationDate() != null) { return false;}
        } else if (!getExpirationDate().equals(aThat.getExpirationDate())) { return false;}

        if (getSubscriptionId() == null) {
            if (aThat.getSubscriptionId() != null) { return false;}
        } else if (!getSubscriptionId().equals(aThat.getSubscriptionId())) { return false;}

        return getPrice() == aThat.getPrice();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = prime * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
        result = prime * result + (getSubscriptionId() != null ? getSubscriptionId().hashCode() : 0);
        result = prime * result + (getPeriodicalEditionId() != null ? getPeriodicalEditionId().hashCode() : 0);
        result = prime * result + getPrice();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", startDate=" + getStartDate() +
                ", expirationDate=" + getExpirationDate() +
                ", price=" + getPrice() +
                ", subscriptionId=" + getSubscriptionId() +
                ", periodicalEditionId=" + getPeriodicalEditionId() +
                '}';
    }
}