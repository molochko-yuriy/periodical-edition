package by.epamtc.periodical_edition.entity;

public abstract class BaseEntity<E> {
    private E id;

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }
}