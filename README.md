# Agent Z communication assistant 🤖

This Quarkus project uses Gemini and google-genai library to generate image from templates. This can allows us to save time for our communication of social network.

## 🙌 Features

- 🚀 **Quarkus Framework**: Fast startup and low memory footprint
- 🤖 **Gemini AI Integration**: Uses Google's latest Gemini models
- 🎨 **Image Description Generation**: Creates detailed descriptions for image generation
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

### Running the Application

**Development Mode (with hot reload):**
```bash
mvn quarkus:dev
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

# Template Configuration
app.template.path=images/template.png
app.template.default=images/template.png
app.template.formats=png,jpg,jpeg

# Quarkus Configuration
quarkus.http.port=8080
quarkus.log.level=INFO
```

### Template Configuration Options

The application now supports configurable templates for image generation:

- **`app.template.path`**: Path to the template image file to use (default: `images/template.png`)
- **`app.template.default`**: Fallback template path if the primary template is not found
- **`app.template.formats`**: Supported image formats (png, jpg, jpeg, gif, webp)

### Using Custom Templates

1. **Place your template images in the `images/` directory**
2. **Configure the template path in `application.properties`:**
   ```properties
   app.template.path=images/my-custom-template.png
   ```
3. **Or pass it as a command-line parameter:**
   ```bash
   java -jar target/quarkus-app/quarkus-run.jar -Dapp.template.path=images/special-template.jpg
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
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

![with love by zenika](https://img.shields.io/badge/With%20%E2%9D%A4%EF%B8%8F%20by-Zenika-b51432.svg?link=https://oss.zenika.com)
