package zenika.marketing.config;

public enum FIELDS_PROMPT {
    NAME("NAME"),
    NAME1("NAME1"),
    NAME2("NAME2"),
    NAME3("NAME3"),
    TITLE("TITLE"),
    PHOTO("PHOTO"),
    PHOTO1("PHOTO1"),
    PHOTO2("PHOTO2"),
    PHOTO3("PHOTO3"),
    TEMPLATE("TEMPLATE"),
    CONF_PHOTO("CONF_PHOTO"),
    PROMPT("PROMPT");

    private final String value;

    FIELDS_PROMPT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
