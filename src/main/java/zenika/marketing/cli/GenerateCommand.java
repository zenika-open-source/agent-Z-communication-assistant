package zenika.marketing.cli;

import picocli.CommandLine.Command;
import zenika.marketing.ui.AgentZCommunication;

@Command(name = "generate", mixinStandardHelpOptions = true, version = "1.0.0", description = "Generate images or videos using Gemini AI.", subcommands = {
        GenerateImageCommand.class,
        GenerateVideoCommand.class,
        AgentZCommunication.class
})
public class GenerateCommand implements Runnable {

    @Override
    public void run() {
        // If no subcommand is provided, display help message
        System.out.println("Please specify a subcommand: image or video");
        System.out.println("Use --help for more information.");
    }
}