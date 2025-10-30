package zenika.marketing.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record Template(String name, String description, String type, String template, String prompt, List<String> fields) {
}

