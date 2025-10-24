package zenika.marketing.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import zenika.marketing.config.MODE_FEATURE;

@RegisterForReflection
public record Template(String name, String description, MODE_FEATURE type, String prompt) {}