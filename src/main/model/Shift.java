package model;

@SuppressWarnings("checkstyle:AvoidEscapedUnicodeCharacters")

public enum Shift {
    DoubleFlat("\uD834\uDD2B"), Flat("♭"), Natural("♮"), Sharp("♯"), DoubleSharp("\uD834\uDD2A");

    private String s;

    Shift(String a) {
        this.s = a;
    }

    public String getString() {
        return s;
    }
}
