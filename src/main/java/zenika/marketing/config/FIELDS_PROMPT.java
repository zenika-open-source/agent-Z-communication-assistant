package zenika.marketing.config;

public enum FIELDS_PROMPT {
    NAME("NAME"),
    TITLE("TITLE"),
    PHOTO("PHOTO"),
    PHOTO2("PHOTO2"),
    TEMPLATE("TEMPLATE"),
    CONF_PHOTO("CONF_PHOTO")
    ;

    private final String value;

    FIELDS_PROMPT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
