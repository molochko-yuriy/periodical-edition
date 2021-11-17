package by.epamtc.periodical_edition.entity;

public class Review extends BaseEntity<Long> {
    private String userComment;
    private int rating;
    private Long userId;
    private Long periodicalEditionId;

    public Review(){}
    public Review(Long id, String userComment, int rating, Long userId, Long periodicalEditionId) {
        super.setId(id);
        this.userComment = userComment;
        this.rating = rating;
        this.userId = userId;
        this.periodicalEditionId = periodicalEditionId;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPeriodicalEditionId() {
        return periodicalEditionId;
    }

    public void setPeriodicalEditionId(Long periodicalEditionId) {
        this.periodicalEditionId = periodicalEditionId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Review aThat = (Review) object;

        if (getId() == null) {
            if (aThat.getId()  != null) { return false;}
        } else if (!getId().equals(aThat.getId())) { return false;}

        if (getUserId() == null) {
            if (aThat.getUserId()  != null) { return false;}
        } else if (!getUserId().equals(aThat.getUserId())) { return false;}

        if (getUserComment() == null) {
            if (aThat.getUserComment()  != null) { return false;}
        } else if (!getUserComment().equals(aThat.getUserComment())) { return false;}

        if (getPeriodicalEditionId() == null) {
            if (aThat.getPeriodicalEditionId()  != null) { return false;}
        } else if (!getPeriodicalEditionId().equals(aThat.getPeriodicalEditionId())) { return false;}

        return getRating() == aThat.getRating();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (getUserComment() != null ? getUserComment().hashCode() : 0);
        result = prime * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = prime * result + (getId() != null ? getId().hashCode() : 0);
        result = prime * result + (getPeriodicalEditionId() != null ? getPeriodicalEditionId().hashCode() : 0);
        result = prime * result + getRating();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", userComment='" + getUserComment() + '\'' +
                ", rating=" + getRating() +
                ", userId=" + getUserId() +
                ", periodicalEditionId=" + getPeriodicalEditionId() +
                '}';
    }
}