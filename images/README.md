# Template Images Directory

This directory contains template images used by the application for image generation.

## Available Templates

- **`template.png`** - Default template (backward compatibility)
- **`template-business.png`** - Business/corporate themed template
- **`template-casual.jpg`** - Casual/informal themed template  
- **`template-creative.webp`** - Creative/artistic themed template

## Usage

Configure which template to use in `application.properties`:

```properties
# Use default template
app.template.path=images/template.png

# Use business template
app.template.path=images/template-business.png

# Use casual template
app.template.path=images/template-casual.jpg

# Use creative template
app.template.path=images/template-creative.webp
```

## Adding New Templates

1. Place your image file in this directory
2. Update the `app.template.path` property to point to your new template
3. Ensure the image format is supported (PNG, JPEG, GIF, WebP)

## Notes

- The application automatically detects the image format based on file extension
- If a template is not found, the application falls back to the default template
- Template files should be actual image files, not text placeholders