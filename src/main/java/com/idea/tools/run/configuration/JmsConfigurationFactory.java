package com.idea.tools.run.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.idea.tools.App.setProject;

public class JmsConfigurationFactory extends ConfigurationFactory {

    protected JmsConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        setProject(project);
        return new JmsRunConfiguration(project, this, "Jms");
    }

    @Override
    public String getName() {
        return "Jms Configuration Factory";
    }
}