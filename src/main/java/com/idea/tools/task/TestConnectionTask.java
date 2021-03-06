package com.idea.tools.task;

import static com.idea.tools.service.JmsService.jmsService;

import com.idea.tools.dto.ServerDto;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class TestConnectionTask extends Task.Backgroundable {

    private final ServerDto server;
    private final Runnable onSuccess;
    private final Consumer<Throwable> onFail;

    public TestConnectionTask(Project project, ServerDto server, Runnable onSuccess, Consumer<Throwable> onFail) {
        super(project, "Test connection");
        this.server = server;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        try {
            jmsService(getProject()).testConnection(server);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onSuccess() {
        onSuccess.run();
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        onFail.accept(error);
    }
}
