# Agent Z communication assistant 🤖

[![Version](https://img.shields.io/badge/version-1.0.0--SNAPSHOT-blue.svg)](https://github.com/zenika-open-source/agent-Z-communication-assistant)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.15.1-blue.svg)](https://quarkus.io/)
[![GitHub](https://img.shields.io/badge/GitHub-zenika--open--source-181717.svg?logo=github)](https://github.com/zenika-open-source/agent-Z-communication-assistant)

This Quarkus project uses Gemini and google-genai library to generate images from templates. This allows us to save time for our social network communication.

> ⚠️ Be careful about the images you use. Please contact the person if you are using their images before using them and integrating them into this API.

## 🙌 Features

- 🚀 **Quarkus Framework**: Fast startup and low memory footprint
- 🤖 **Gemini AI Integration**: Uses Google's latest Gemini models
- 🎨 **Image Generation**: Creates images from pre-configured templates for blog posts and conference speakers
- 🎥 **Video Generation**: Generate videos with Veo models
- 💻 **Picocli CLI**: Rich command-line interface with comprehensive options
- 📝 **Post Generation**: Generate LinkedIn & Bluesky posts
- 🎯 **Multiple Templates**: Support for single and dual-author/speaker templates

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

### Available Templates

The application provides several pre-configured templates for different use cases:

#### Image Templates

1. **`generate-image-blog-post`** - Generate an image for a blog post (single author)
   - Fields: `NAME`, `TITLE`, `PHOTO`

2. **`generate-image-2-blog-post`** - Generate an image for a blog post with 2 authors
   - Fields: `NAME`, `NAME2`, `TITLE`, `PHOTO`, `PHOTO2`

3. **`generate-image-speaker-event`** - Generate an image to announce a speaker for a conference
   - Fields: `NAME`, `TITLE`, `CONF_PHOTO`, `PHOTO`

4. **`generate-image-2-speaker-event`** - Generate an image to announce a talk with 2 speakers
   - Fields: `NAME`, `NAME2`, `TITLE`, `PHOTO`, `PHOTO2`, `CONF_PHOTO`

5. **`generate-image-from-prompt`** - Generate an image from a prompt
   - Fields: `PROMPT`

6. **`generate-image-duck-from-prompt`** - Generate an image from a prompt with the Zenika duck
   - Fields: `PROMPT`

#### Video Templates

5. **`generate-video-speaker-event`** - Generate a video to announce a speaker for a conference

#### Text Templates

6. **`generate-post-speaker-event`** - Generate LinkedIn & Bluesky posts

### Command Line Interface

**Quick examples:**

```bash
# Show help
quarkus dev -Dquarkus.args="--help"

# Generate image for a blog post (single author)
quarkus dev -Dquarkus.args="image --template-name generate-image-blog-post --title=IntroductiontoDuckDB --name=John_Doe --photo=images/people/john-doe.png -o output.png"

# Generate image for a blog post (2 authors)
quarkus dev -Dquarkus.args="image --template-name generate-image-2-blog-post --title=Exploring_Firebas_Studio --name=Alice-Smith --name2=Bob_Johnson --photo=images/people/alice.png --photo2 images/people/bob.png -o output.png"

# Generate image for a conference speaker
quarkus dev -Dquarkus.args="image --template-name generate-image-speaker-event --title=My_Great_Talk --name=Speaker_Name --photo=images/people/speaker.png --conf-photo=images/logos/conference.png -o output.png"

# Generate image for a conference with 2 speakers
quarkus dev -Dquarkus.args="image --template-name generate-image-2-speaker-event --title=Firebase_Studio --name=Speaker_1 --name2=Speaker_2 --photo images/people/peolple1.png --photo2=images/people/people2.png --conf-photo=images/logos/conference.png -o output.png"

# Generate image from a prompt
quarkus dev -Dquarkus.args="image --template-name generate-image-from-prompt --prompt='A futuristic city with flying cars' -o output.png"

# Generate video
quarkus dev -Dquarkus.args="video --prompt 'Conference intro' --vertex"
```

### Running the Application

**With CLI arguments:**

```bash
quarkus dev -Dquarkus.args="--type image --prompt 'Your prompt here' --output result.png"
```

**Production mode:**

```bash
# Build
mvn clean package

# Run
java -jar target/quarkus-app/quarkus-run.jar --help

# Example: Generate image for a speaker event
java -jar target/quarkus-app/quarkus-run.jar image --template-name generate-image-speaker-event --title "My Great Talk" --name "Speaker Name" --photo images/people/speaker-photo.png --conf-photo images/logos/conference.png -o output.png
```

## Configuration

The application can be configured through `src/main/resources/application.properties`:

```properties
# Google AI Configuration
google.ai.api.key=your-api-key-here

# Gemini Model Settings
app.gemini.model=gemini-2.0-flash-exp
app.gemini.model.veo=veo-2.0
app.result.filename=gemini-generation-image.png

# Video Generation Configuration
app.video.ratio=16:9
app.video.resolution=1080p

# Quarkus Configuration
quarkus.http.port=8080
quarkus.log.level=INFO
```

### Key Configuration Options

- **`google.ai.api.key`**: Your Google AI API key (required)
- **`app.gemini.model`**: Gemini model for image generation (default: `gemini-2.0-flash-exp`)
- **`app.gemini.model.veo`**: Veo model for video generation (default: `veo-2.0`)
- **`app.result.filename`**: Default output filename for generated images
- **`app.video.ratio`**: Video aspect ratio (e.g., `16:9`, `9:16`, `1:1`)
- **`app.video.resolution`**: Video resolution (e.g., `1080p`, `720p`, `4k`)

### Templates

Templates are defined in `src/main/resources/templates.json`. Each template specifies:
- **name**: Unique identifier for the template
- **description**: Human-readable description
- **type**: Template type (`IMAGE`, `VIDEO`, or `POST`)
- **template**: Path to the template image file
- **fields**: Required fields for the template
- **prompt**: AI prompt used to generate the content

To add a new template, edit `templates.json` and add a corresponding handler in `GenerateImageCommand.java`.

### Supported Image Formats

The application automatically detects and supports the following image formats:

- **PNG** (`.png`) - Recommended for templates with transparency
- **JPEG** (`.jpg`, `.jpeg`) - Good for photographic templates
- **GIF** (`.gif`) - Supports animated templates
- **WebP** (`.webp`) - Modern format with good compression

## 🙌 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## ⁉️ Support

For support and questions:

- Create an issue in the GitHub repository
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
