# Agent Z communication assistant 🤖

This Quarkus project uses Gemini and google-genai library to generate image from templates. This can allows us to save time for our communication of social network.

> ⚠️ Be careful about the images you use. Please contact the person if you are using their images before using them and integrating them into this API.

## 🙌 Features

- 🚀 **Quarkus Framework**: Fast startup and low memory footprint
- 🤖 **Gemini AI Integration**: Uses Google's latest Gemini models
- 🎨 **Image Generation**: Creates images from templates and prompts
- 🎥 **Video Generation**: Generate videos with Veo models
- 💻 **Picocli CLI**: Rich command-line interface with comprehensive options
- 📝 **Text Generation**: General-purpose text generation capabilities

## 🛠️ Prerequisites

- Java 17 or higher
- Maven 3.8+
- Google AI API Key

## 👨‍💻 Installation

1. **Clone the repository**

   ```bash
   git clone git@github.com:zenika-open-source/agent-Z-communication-assistant.git
   cd agent-Z-communication-assistant
   ```

2. **Set up your Google AI API Key**

   Option A: Environment Variable (Recommended)

   ```bash
   export GOOGLE_API_KEY=your-api-key-here
   ```

   Option B: Configuration File

   ```bash
   # Edit src/main/resources/application.properties
   google.ai.api.key=your-api-key-here
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

## 🚀 Usage

### Command Line Interface (Picocli)

The application now provides a rich CLI with Picocli. For detailed CLI documentation, see [CLI.md](CLI.md).

**Quick examples:**

```bash
# Show help
mvn quarkus:dev -Dquarkus.args="--help"

# Generate image with custom prompt
quarkus dev -Dquarkus.args='image --template-name=generate-image-blog-post --title=DuckDB --name=zMember -o=outpu.png --z-photo=images/people/my-z-member.png'

# Generate video
mvn quarkus:dev -Dquarkus.args="-t video --prompt 'Conference intro' --vertex"
```

### Running the Application

**Development Mode (with hot reload):**

```bash
mvn quarkus:dev
```

**With CLI arguments:**

```bash
mvn quarkus:dev -Dquarkus.args="--type image --prompt 'Your prompt here' --output result.png"
```

**Production mode:**

```bash
# Build
mvn clean package

# Run
java -jar target/quarkus-app/quarkus-run.jar --help
```

## Configuration

The application can be configured through `src/main/resources/application.properties`:

```properties
# Google AI Configuration
google.ai.api.key=your-api-key-here

# Gemini Model Settings
app.gemini.model=gemini-2.5-flash-image-preview
app.result.filename=gemini-generation-image.png
app.prompt=Your custom prompt for image generation

# Media Type Configuration
app.media.type=image
# Supported values: image, video

# Template and Files Configuration
app.template.path=images/template.png
app.file1.path=images/file1.png
app.file2.path=images/file2.png
app.template.formats=png,jpg,jpeg

# Video Generation Configuration (for video media type)
app.video.ratio=16:9
app.video.resolution=1080p

# Quarkus Configuration
quarkus.http.port=8080
quarkus.log.level=INFO
```

### Template Configuration Options

The application supports two main modes: **Image Generation** and **Video Generation**.

#### Image Generation Mode

- **`app.media.type`**: Set to `image` for image generation (default)
- **`app.gemini.model`**: Gemini model for image generation (e.g., `gemini-2.5-flash-image`)
- **`app.template.path`**: Path to the template image file to use
- **`app.file1.path`**: Path to the first additional image (e.g., conference logo)
- **`app.file2.path`**: Path to the second additional image (e.g., speaker photo)
- **`app.template.formats`**: Supported image formats (png, jpg, jpeg, gif, webp)

#### Video Generation Mode

- **`app.media.type`**: Set to `video` for video generation
- **`app.gemini.model.veo`**: Veo model for video generation (e.g., `veo-3.0-fast-generate-001`)
- **`app.video.ratio`**: Video aspect ratio (e.g., `16:9`, `9:16`, `1:1`)
- **`app.video.resolution`**: Video resolution (e.g., `1080p`, `720p`, `4k`)
- **`app.template.path`**: Path to the base image for video generation

### Using Custom Templates

1. **Place your template images in the `images/` directory**
2. **Configure the template path in `application.properties`:**

   ```properties
   # For image generation
   app.media.type=image
   app.template.path=images/my-custom-template.png
   app.file1.path=images/logo.png
   app.file2.path=images/speaker-photo.jpg

   # For video generation
   app.media.type=video
   app.template.path=images/base-image.png
   app.video.ratio=16:9
   app.video.resolution=1080p
   ```
3. **Or pass it as a command-line parameter:**
   ```bash
   # Image generation
   java -jar target/quarkus-app/quarkus-run.jar \
     -Dapp.media.type=image \
     -Dapp.template.path=images/special-template.jpg \
     -Dapp.file1.path=images/conference-logo.png \
     -Dapp.file2.path=images/speaker.jpg

   # Video generation
   java -jar target/quarkus-app/quarkus-run.jar \
     -Dapp.media.type=video \
     -Dapp.template.path=images/base.png \
     -Dapp.video.ratio=9:16 \
     -Dapp.video.resolution=1080p
   ```

### Supported Template Formats

The application automatically detects and supports the following image formats:

- **PNG** (`.png`) - Recommended for templates with transparency
- **JPEG** (`.jpg`, `.jpeg`) - Good for photographic templates
- **GIF** (`.gif`) - Supports animated templates
- **WebP** (`.webp`) - Modern format with good compression

### Template Validation

The application includes built-in template validation:

- Checks if the specified template file exists
- Falls back to the default template if the primary template is missing
- Provides clear error messages if no valid template is found
- Logs template usage for debugging

## 🙌 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## ⁉️ Support

For support and questions:

- Create an issue in the GitLab repository
- Check the [Google AI documentation](https://ai.google.dev/docs)
- Review the [Quarkus guides](https://quarkus.io/guides/)

## 📒 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙍 Authors

- Jean-Philippe Baconnais (@jeanphi-baconnais)

## 🙏 Contributors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://jeanphi-baconnais.gitlab.io/"><img src="https://avatars.githubusercontent.com/u/32639372?v=4" width="100px;" alt=""/><br /><sub><b>Jean-Phi Baconnais</b></sub></a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://www.bbourgeois.dev"><img src="https://avatars.githubusercontent.com/u/20949060?v=4" width="100px;" alt=""/><br /><sub><b>Benjamin</b></sub></a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

![with love by zenika](https://img.shields.io/badge/With%20%E2%9D%A4%EF%B8%8F%20by-Zenika-b51432.svg?link=https://oss.zenika.com)
