package model;

public enum NoteName {
    A("A"), B("B"), C("C"), D("D"), E("E"), F("F"), G("G");

    private String s;

    NoteName(String a) {
        this.s = a;
    }

    public String getString() {
        return s;
    }
}

