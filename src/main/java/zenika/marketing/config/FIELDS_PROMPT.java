package zenika.marketing.config;

public enum FIELDS_PROMPT {
    NAME("NAME"),
    TITLE("TITLE"),
    Z_PHOTO("Z_PHOTO"),
    TEMPLATE("TEMPLATE");

    private final String value;

    FIELDS_PROMPT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
