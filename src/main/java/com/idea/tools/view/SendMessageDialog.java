package com.idea.tools.view;

import com.idea.tools.dto.DestinationDto;
import com.idea.tools.dto.MessageDto;
import com.idea.tools.task.SendMessageTask;
import com.idea.tools.utils.GuiUtils;
import com.idea.tools.view.components.message.SendMessageHeadersPanel;
import com.idea.tools.view.components.message.SendMessageMainPanel;
import com.idea.tools.view.components.message.SendMessagePayloadPanel;
import com.idea.tools.view.components.message.ViewMessageHeadersPanel;
import com.idea.tools.view.components.message.ViewMessageMainPanel;
import com.idea.tools.view.components.message.ViewMessagePayloadPanel;
import com.intellij.openapi.project.Project;
import java.util.Optional;
import java.util.function.Supplier;
import javax.swing.*;

public class SendMessageDialog extends ViewMessageDialog {

	private final Project project;

	protected SendMessageDialog(Project project, MessageDto message) {
		super(project, message);
		this.project = project;
		render();
	}

	private SendMessageDialog(Project project, DestinationDto destination) {
		super(project, destination);
		this.project = project;
		render();
	}

	public static void showDialog(Project project, DestinationDto queue) {
		GuiUtils.showDialog(new SendMessageDialog(project, queue), "Send message");
	}

	public static void showDialog(Project project, MessageDto message) {
		GuiUtils.showDialog(new SendMessageDialog(project, message), "Send message");
	}

	private void render() { }

	@Override
	protected void closeAfterSendCheckBox(JCheckBox closeAfterSendCheckBox) {
	}

	@Override
	protected void actionButton(JButton actionButton) {
		actionButton.addActionListener(event -> {
			MessageDto msg = new MessageDto();
			fillMessage(msg);
			new SendMessageTask(project, msg, () -> {
				if (closeAfterSendCheckBox.isSelected()) {
					dispose();
				}
			}).queue();
		});
	}

	@Override
	protected ViewMessageMainPanel mainPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
		Supplier<RuntimeException> runtimeException =
				() -> new IllegalArgumentException("MainPanel may not be initialized neither message or destination is present");

		return message.map(SendMessageMainPanel::new)
				.orElseGet(() -> destination.map(SendMessageMainPanel::new)
						.orElseThrow(runtimeException));
	}

	@Override
	protected ViewMessageHeadersPanel headersPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
		return message.map(SendMessageHeadersPanel::new).orElseGet(SendMessageHeadersPanel::new);
	}

	@Override
	protected ViewMessagePayloadPanel payloadPanel(Optional<DestinationDto> destination, Optional<MessageDto> message) {
		return message.map(SendMessagePayloadPanel::new).orElseGet(SendMessagePayloadPanel::new);
	}

}
