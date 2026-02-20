package zenika.marketing.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConfigProperties {

    @ConfigProperty(name = "app.gemini.model.image")
    String defaultGeminiModelImage;

    @ConfigProperty(name = "app.gemini.model.veo")
    String defaultGeminiVeoModel;

    @ConfigProperty(name = "app.gemini.model")
    String defaultGeminiModel;

    @ConfigProperty(name = "app.result.filename")
    String defaultResultFilename;

    @ConfigProperty(name = "app.result.filename.video")
    String defaultResultFilenameVideo;

    @ConfigProperty(name = "app.prompt")
    String defaultPrompt;

    @ConfigProperty(name = "app.template.path", defaultValue = "")
    String defaultTemplatePath;

    @ConfigProperty(name = "app.file1.path", defaultValue = "")
    String defaultFile1Path;

    @ConfigProperty(name = "app.file2.path", defaultValue = "")
    String defaultFile2Path;

    @ConfigProperty(name = "app.video.ratio", defaultValue = "16:9")
    String defaultVideoRatio;

    @ConfigProperty(name = "app.video.resolution", defaultValue = "1080p")
    String defaultVideoResolution;

    @ConfigProperty(name = "app.name", defaultValue = "")
    String defaultName;

    @ConfigProperty(name = "app.name2", defaultValue = "")
    String defaultName2;

    @ConfigProperty(name = "app.name3", defaultValue = "")
    String defaultName3;

    @ConfigProperty(name = "app.title", defaultValue = "")
    String defaultTitle;

    @ConfigProperty(name = "app.photo", defaultValue = "")
    String defaultPhoto;

    @ConfigProperty(name = "app.photo2", defaultValue = "")
    String defaultPhoto2;

    @ConfigProperty(name = "app.photo3", defaultValue = "")
    String defaultPhoto3;

    @ConfigProperty(name = "app.conf_photo", defaultValue = "")
    String defaultConfPhoto;

    @ConfigProperty(name = "app.conf", defaultValue = "")
    String defaultConf;

    @ConfigProperty(name = "app.job_title", defaultValue = "")
    String defaultJobTitle;

    @ConfigProperty(name = "app.city", defaultValue = "")
    String defaultCity;

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

    public String getDefaultConf() {
        return defaultConf;
    }

    public String getDefaultResultFilenameVideo() {
        return defaultResultFilenameVideo;
    }

    public String getDefaultJobTitle() {
        return defaultJobTitle;
    }

    public String getDefaultCity() {
        return defaultCity;
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

    public void setDefaultConf(String defaultConf) {
        this.defaultConf = defaultConf;
    }

    public void setDefaultResultFilenameVideo(String defaultResultFilenameVideo) {
        this.defaultResultFilenameVideo = defaultResultFilenameVideo;
    }

    public void setDefaultJobTitle(String defaultJobTitle) {
        this.defaultJobTitle = defaultJobTitle;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
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
            case CONF -> config.getDefaultConf();
            case PROMPT -> config.getDefaultPrompt();
            case JOB_TITLE -> config.getDefaultJobTitle();
            case CITY -> config.getDefaultCity();
            default -> "";
        };
    }

    public String getDefaultGeminiModel() {
        return this.defaultGeminiModel;
    }

    public void setDefaultGeminiModel(String model) {
        this.defaultGeminiModel = model;
    }

}
