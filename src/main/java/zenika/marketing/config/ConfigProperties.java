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

    @ConfigProperty(name = "app.result.filename.video")
    String defaultResultFilenameVideo;

    @ConfigProperty(name = "app.prompt")
    String defaultPrompt;

    String defaultTemplatePath;

    String defaultFile1Path;

    String defaultFile2Path;

    @ConfigProperty(name = "app.video.ratio")
    String defaultVideoRatio;

    @ConfigProperty(name = "app.video.resolution")
    String defaultVideoResolution;

    String defaultName;

    String defaultName2;

    String defaultName3;

    String defaultTitle;

    String defaultPhoto;

    String defaultPhoto2;

    String defaultPhoto3;

    String defaultConfPhoto;

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

    public String getDefaultName() {
        return defaultName;
    }

    public String getDefaultName2() {
        return defaultName2;
    }

    public String getDefaultName3() {
        return defaultName3;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public String getDefaultPhoto() {
        return defaultPhoto;
    }

    public String getDefaultPhoto2() {
        return defaultPhoto2;
    }

    public String getDefaultPhoto3() {
        return defaultPhoto3;
    }

    public String getDefaultConfPhoto() {
        return defaultConfPhoto;
    }

    public String getDefaultResultFilenameVideo() {
        return defaultResultFilenameVideo;
    }

    public void setDefaultPhoto2(String defaultPhoto2) {
        this.defaultPhoto2 = defaultPhoto2;
    }

    public void setDefaultPhoto3(String defaultPhoto3) {
        this.defaultPhoto3 = defaultPhoto3;
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

    public void setDefaultName2(String defaultName2) {
        this.defaultName2 = defaultName2;
    }

    public void setDefaultName3(String defaultName3) {
        this.defaultName3 = defaultName3;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public void setZPhoto(String defaultZPhoto) {
        this.defaultPhoto = defaultZPhoto;
    }

    public void setDefaultConfPhoto(String defaultConfPhoto) {
        this.defaultConfPhoto = defaultConfPhoto;
    }

    public void setDefaultResultFilenameVideo(String defaultResultFilenameVideo) {
        this.defaultResultFilenameVideo = defaultResultFilenameVideo;
    }

    public String getFieldByValue(String field, ConfigProperties config) {
        return switch (FIELDS_PROMPT.valueOf(field)) {
            case NAME, NAME1 -> config.getDefaultName();
            case NAME2 -> config.getDefaultName2();
            case NAME3 -> config.getDefaultName3();
            case TITLE -> config.getDefaultTitle();
            case TEMPLATE -> config.getDefaultTemplatePath();
            case PHOTO, PHOTO1 -> config.getDefaultPhoto();
            case PHOTO2 -> config.getDefaultPhoto2();
            case PHOTO3 -> config.getDefaultPhoto3();
            case CONF_PHOTO -> config.getDefaultConfPhoto();
            case PROMPT -> config.getDefaultPrompt();
            default -> "";
        };
    }

}
