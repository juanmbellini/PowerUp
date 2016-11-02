package ar.edu.itba.paw.webapp.model;


public enum FilterCategory {
    publisher,
    developer,
    genre,
    keyword,
    platform;

    //TODO delete if unused
    public String pretty() {
        return ((Character)(name().charAt(0))).toString().toUpperCase() + name().substring(1);
    }
}
