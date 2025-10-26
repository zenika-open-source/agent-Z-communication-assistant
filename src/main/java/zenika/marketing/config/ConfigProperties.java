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
}
