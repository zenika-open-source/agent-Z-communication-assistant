package zenika.marketing.config;


import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConfigProperties {

    @ConfigProperty(name = "app.gemini.model")
    String defaultGeminiModelImage;

    @ConfigProperty(name = "app.gemini.model.veo")
    String defaultGeminiVeoModel;

    @ConfigProperty(name = "app.result.filename")
    String defaultResultFilename;

    @ConfigProperty(name = "app.prompt")
    String defaultPrompt;

    @ConfigProperty(name = "app.template.path")
    String defaultTemplatePath;

    @ConfigProperty(name = "app.file1.path")
    String defaultFile1Path;

    @ConfigProperty(name = "app.file2.path")
    String defaultFile2Path;

    @ConfigProperty(name = "app.video.ratio")
    String defaultVideoRatio;

    @ConfigProperty(name = "app.video.resolution")
    String defaultVideoResolution;

    @ConfigProperty(name = "app.name")
    String defaultName;

    @ConfigProperty(name = "app.title")
    String defaultTitle;

    @ConfigProperty(name = "app.photo")
    String defaultPhoto;

    @ConfigProperty(name = "app.photo2")
    String defaultPhoto2;

    public String getDefaultGeminiModelImage() {
        return defaultGeminiModelImage;
    }

    public String getDefaultGeminiVeoModel() {
        return defaultGeminiVeoModel;
    }

    public String getDefaultResultFilename() {
        return defaultResultFilename;
    }

    public String getDefaultPrompt() {
        return defaultPrompt;
    }

    public String getDefaultTemplatePath() {
        return defaultTemplatePath;
    }

    public String getDefaultFile1Path() {
        return defaultFile1Path;
    }

    public String getDefaultFile2Path() {
        return defaultFile2Path;
    }

    public String getDefaultVideoRatio() {
        return defaultVideoRatio;
    }

    public String getDefaultVideoResolution() {
        return defaultVideoResolution;
    }

    public String getDefaultName() { return defaultName; }

    public String getDefaultTitle() { return defaultTitle; }

    public String getDefaultPhoto() { return defaultPhoto; }

    public void setDefaultPhoto2(String defaultPhoto2) {
        this.defaultPhoto2 = defaultPhoto2;
    }

    public void setDefaultPhoto(String defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public void setDefaultGeminiModelImage(String defaultGeminiModelImage) {
        this.defaultGeminiModelImage = defaultGeminiModelImage;
    }

    public void setDefaultGeminiVeoModel(String defaultGeminiVeoModel) {
        this.defaultGeminiVeoModel = defaultGeminiVeoModel;
    }

    public void setDefaultResultFilename(String defaultResultFilename) {
        this.defaultResultFilename = defaultResultFilename;
    }

    public void setDefaultPrompt(String defaultPrompt) {
        this.defaultPrompt = defaultPrompt;
    }

    public void setDefaultTemplatePath(String defaultTemplatePath) {
        this.defaultTemplatePath = defaultTemplatePath;
    }

    public void setDefaultFile1Path(String defaultFile1Path) {
        this.defaultFile1Path = defaultFile1Path;
    }

    public void setDefaultFile2Path(String defaultFile2Path) {
        this.defaultFile2Path = defaultFile2Path;
    }

    public void setDefaultVideoRatio(String defaultVideoRatio) {
        this.defaultVideoRatio = defaultVideoRatio;
    }

    public void setDefaultVideoResolution(String defaultVideoResolution) {
        this.defaultVideoResolution = defaultVideoResolution;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public void setZPhoto(String defaultZPhoto) {
        this.defaultPhoto = defaultZPhoto;
    }

    public String getDefaultPhoto2() {
        return defaultPhoto2;
    }

    public String getFieldByValue(String field, ConfigProperties config) {
        return switch (FIELDS_PROMPT.valueOf(field)) {
            case NAME -> config.getDefaultName();
            case TITLE -> config.getDefaultTitle();
            case TEMPLATE -> config.getDefaultTemplatePath();
            case PHOTO -> config.getDefaultPhoto();
            case PHOTO2 -> config.getDefaultPhoto2();
            default -> "";
        };
    }

}
