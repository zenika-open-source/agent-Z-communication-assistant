package zenika.marketing.cli;

import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.config.MODE_FEATURE;
import zenika.marketing.domain.Template;
import zenika.marketing.services.GeminiImagesServices;
import zenika.marketing.services.TemplateService;
import zenika.marketing.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

@Command(name = "image", mixinStandardHelpOptions = true, description = "Generate an image using Gemini AI.")
public class GenerateImageCommand implements Runnable {

    @Inject
    GeminiImagesServices geminiServices;

    @Inject
    ConfigProperties config;

    @Inject
    TemplateService templateService;

    @Option(names = { "-o", "--output" }, description = "Output filename")
    String output;

    @Option(names = { "--template" }, description = "Name of the template to use")
    String template;

    @Option(names = { "--name" }, description = "Speaker or writer Name")
    String name;

    @Option(names = { "--name2" }, description = "Speaker or writer Name")
    String name2;

    @Option(names = { "--title" }, description = "Blog post or talk title")
    String title;

    @Option(names = { "-m", "--model" }, description = "Gemini model to use")
    String model;

    @Option(names = { "--photo" }, description = "First photo")
    String photo;

    @Option(names = { "--photo2" }, description = "Second photo")
    String photo2;

    @Option(names = { "--conf-photo" }, description = "Conference logo")
    String confPhoto;

    @Option(names = { "--template-name" }, description = "Name of the template to use", required = true)
    String templateName;

    private final Map<String, Consumer<Template>> templateHandlers = Map.of(
            "generate-image-blog-post", this::generateImageBlogPost,
            "generate-image-2-blog-post", this::generateImage2BlogPost,
            "generate-image-speaker-event", this::generateImageSpeakerEvent,
            "generate-image-2-speaker-event", this::generateImage2SpeakerEvent
    // generate-post-speaker-event
    );

    @Override
    public void run() {
        try {
            var template = templateService.waitAValidTemplateByUser(templateName);

            if (!template.type().equals(MODE_FEATURE.IMAGE.toString())) {
                Log.error(
                        "❌ Error: Template '" + templateName + "' is not an IMAGE template (" + template.type() + ")");
                System.exit(1);
            }

            Consumer<Template> handler = templateHandlers.get(template.name());
            if (handler == null) {
                Log.error("❌ Error: No handler found for template '" + template.name()
                        + "'. Please implement it in GenerateImageCommand.");
                System.exit(1);
            }
            handler.accept(template);

            System.exit(0);
        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Generate an image for a blog post template.
     * - 1 title
     * - 1 writer
     * - 1 writer photo
     *
     * @param template: template configuration
     */
    private void generateImageBlogPost(Template template) {
        Content content = null;
        config.setDefaultName(name != null ? name : config.getDefaultName());
        config.setDefaultTitle(title != null ? title : config.getDefaultTitle());
        config.setDefaultPhoto(photo);

        String completedPrompt = templateService.preparePrompt(template, config);

        Path templateFile = Path.of(template.template());
        Path zPhoto = Path.of(config.getDefaultPhoto());

        checkisFileExist(templateFile);
        checkisFileExist(zPhoto);

        try {
            content = Content.fromParts(
                    Part.fromBytes(Files.readAllBytes(templateFile), Utils.getMimeType(templateFile.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto), Utils.getMimeType(zPhoto.toString())),
                    Part.fromText(completedPrompt));
        } catch (IOException e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }

        prepareCallGemini(template, content, completedPrompt);
    }

    private void generateImage2BlogPost(Template template) {
        Content content = null;
        config.setDefaultName(name != null ? name : config.getDefaultName());
        config.setDefaultName2(name2 != null ? name2 : config.getDefaultName2());
        config.setDefaultTitle(title != null ? title : config.getDefaultTitle());
        config.setDefaultPhoto(photo);
        config.setDefaultPhoto2(photo2);

        String completedPrompt = templateService.preparePrompt(template, config);

        Path templateFile = Path.of(template.template());
        Path zPhoto = Path.of(config.getDefaultPhoto());
        Path zPhoto2 = Path.of(config.getDefaultPhoto2());

        checkisFileExist(templateFile);
        checkisFileExist(zPhoto);
        checkisFileExist(zPhoto2);

        try {
            content = Content.fromParts(
                    Part.fromBytes(Files.readAllBytes(templateFile), Utils.getMimeType(templateFile.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto), Utils.getMimeType(zPhoto.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto2), Utils.getMimeType(zPhoto2.toString())),
                    Part.fromText(completedPrompt));
        } catch (IOException e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }

        prepareCallGemini(template, content, completedPrompt);
    }

    private void checkisFileExist(Path pathFile) {
        if (!Files.exists(pathFile)) {
            Log.error("❌ File not found: " + pathFile);
            System.exit(1);
        }
    }

    private void generateImageSpeakerEvent(Template template) {
        Content content = null;
        config.setDefaultName(name != null ? name : config.getDefaultName());
        config.setDefaultTitle(title != null ? title : config.getDefaultTitle());
        config.setDefaultPhoto(photo);

        String completedPrompt = templateService.preparePrompt(template, config);

        Path templateFile = Path.of(template.template());
        Path zPhoto = Path.of(config.getDefaultPhoto());

        checkisFileExist(templateFile);
        checkisFileExist(zPhoto);

        try {
            content = Content.fromParts(
                    Part.fromBytes(Files.readAllBytes(templateFile), Utils.getMimeType(templateFile.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto), Utils.getMimeType(zPhoto.toString())),
                    Part.fromText(completedPrompt));
        } catch (IOException e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }

        prepareCallGemini(template, content, completedPrompt);
    }

    private void generateImage2SpeakerEvent(Template template) {
        Content content = null;
        config.setDefaultName(name != null ? name : config.getDefaultName());
        config.setDefaultName2(name2 != null ? name2 : config.getDefaultName2());
        config.setDefaultTitle(title != null ? title : config.getDefaultTitle());
        config.setDefaultPhoto(photo);
        config.setDefaultPhoto2(photo2);
        config.setDefaultConfPhoto(confPhoto);

        String completedPrompt = templateService.preparePrompt(template, config);

        Path templateFile = Path.of(template.template());
        Path zPhoto = Path.of(config.getDefaultPhoto());
        Path zPhoto2 = Path.of(config.getDefaultPhoto2());
        Path confPhoto = Path.of(config.getDefaultConfPhoto());

        checkisFileExist(templateFile);
        checkisFileExist(zPhoto);
        checkisFileExist(zPhoto2);
        checkisFileExist(confPhoto);

        try {
            content = Content.fromParts(
                    Part.fromBytes(Files.readAllBytes(templateFile), Utils.getMimeType(templateFile.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto), Utils.getMimeType(zPhoto.toString())),
                    Part.fromBytes(Files.readAllBytes(zPhoto2), Utils.getMimeType(zPhoto2.toString())),
                    Part.fromBytes(Files.readAllBytes(confPhoto), Utils.getMimeType(confPhoto.toString())),
                    Part.fromText(completedPrompt));
        } catch (IOException e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }

        prepareCallGemini(template, content, completedPrompt);
    }

    private void prepareCallGemini(Template template, Content content, String prompt) {
        config.setDefaultResultFilename(output != null ? output : config.getDefaultResultFilename());
        config.setDefaultResultFilename(
                String.valueOf(System.currentTimeMillis()).concat("-").concat(config.getDefaultResultFilename()));

        String finalModel = model != null ? model : config.getDefaultGeminiModelImage();

        Log.infof("-> generateImageBlogPost %s", template.name());
        Log.info("\uD83D\uDCDD \uD83D\uDC49 Prompt: \n \t " + prompt + "\n");

        try {
            geminiServices.generateImage(finalModel, config, content);
        } catch (IOException e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
