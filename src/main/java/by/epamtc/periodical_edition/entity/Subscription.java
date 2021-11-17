package by.epamtc.periodical_edition.entity;

import by.epamtc.periodical_edition.enums.PaymentStatus;

public class Subscription extends BaseEntity<Long> {
    private int price;
    private Long userId;
    private PaymentStatus paymentStatus;

    public  Subscription(){}

    public Subscription(Long id, int price, Long userId, PaymentStatus paymentStatus) {
        super.setId(id);
        this.price = price;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Subscription aThat = (Subscription) object;

        if (getId() == null) {
            if (aThat.getId()  != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getUserId() == null) {
            if (aThat.getUserId()  != null) { return false;}
        } else if (!getUserId().equals(aThat.getUserId())) { return false;}

        if (getPaymentStatus() == null) {
            if (aThat.getPaymentStatus()  != null) { return false;}
        } else if (!getPaymentStatus().equals(aThat.getPaymentStatus())) { return false;}

        return getPrice() == aThat.getPrice();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = prime * result + (getPaymentStatus() != null ? getPaymentStatus().hashCode() : 0);
        result = prime * result + getPrice();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", paymentStatus=" + getPaymentStatus() +
                ", userId=" + getUserId() +
                '}';
    }
}