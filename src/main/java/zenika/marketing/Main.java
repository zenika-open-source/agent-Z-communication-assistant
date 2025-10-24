package zenika.marketing;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;
import zenika.marketing.cli.GenerateCommand;

import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.services.GeminiServices;
import zenika.marketing.services.TemplateService;

@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    CommandLine.IFactory factory;

    @Inject
    GeminiServices geminiServices;

    @Inject
    TemplateService templateService;

    @Inject
    ConfigProperties config;

    public static void main(String... args) {
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(new GenerateCommand(geminiServices, config, templateService), factory).execute(args);
    }
}