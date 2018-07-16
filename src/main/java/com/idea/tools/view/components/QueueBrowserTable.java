package com.idea.tools.view.components;

import com.idea.tools.dto.MessageDto;
import com.idea.tools.dto.QueueDto;
import com.idea.tools.view.button.MessagesReloadButton;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.idea.tools.App.jmsService;
import static com.idea.tools.utils.GuiUtils.showYesNoDialog;
import static com.intellij.openapi.actionSystem.ActionToolbarPosition.TOP;
import static com.intellij.ui.ToolbarDecorator.createDecorator;
import static java.awt.BorderLayout.CENTER;
import static java.util.Collections.emptyList;

public class QueueBrowserTable extends AddEditRemovePanel<MessageDto> {

    private static final Logger LOGGER = Logger.getInstance(QueueBrowserTable.class);

    @Getter
    private QueueDto queue;

    public QueueBrowserTable(QueueDto queue) {
        super(new MyTableModel(), emptyList());
        this.queue = queue;
        render();
    }

    //TODO row select mode
    private void render() {
        JBTable table = getTable();
        TableColumnModel model = table.getColumnModel();

        TableColumn idColumn = model.getColumn(0);
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(200);

        TableColumn timestampColumn = model.getColumn(1);
        timestampColumn.setMinWidth(80);
        timestampColumn.setMaxWidth(150);

        TableColumn typeColumn = model.getColumn(2);
        typeColumn.setMinWidth(30);
        typeColumn.setMaxWidth(60);

        TableColumn deliveryModeColumn = model.getColumn(3);
        deliveryModeColumn.setMinWidth(30);
        deliveryModeColumn.setMaxWidth(90);

        TableColumn priorityColumn = model.getColumn(4);
        priorityColumn.setMinWidth(30);
        priorityColumn.setMaxWidth(60);

        TableColumn expirationColumn = model.getColumn(5);
        expirationColumn.setMinWidth(30);
        expirationColumn.setMaxWidth(60);

        TableColumn payloadColumn = model.getColumn(6);
        payloadColumn.setMinWidth(100);
        payloadColumn.setMaxWidth(1500);

        table.setShowColumns(true);
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(true);
    }

    @Override
    protected void initPanel() {
        setLayout(new BorderLayout());
        ToolbarDecorator decorator = createDecorator(getTable())
                .setMoveDownAction(button -> doDown())
                .setMoveUpAction(button -> doUp())
                .setRemoveAction(button -> doRemove())
                .addExtraAction(new MessagesReloadButton(this))
                .setToolbarPosition(TOP);

        JPanel panel = decorator.createPanel();
        add(panel, CENTER);
    }

    @Nullable
    @Override
    protected MessageDto addItem() {
        return null;
    }

    @Override
    protected boolean removeItem(MessageDto msg) {
        boolean delete = showYesNoDialog("Delete message from the queue?");
        try {
            return delete && jmsService().removeFromQueue(msg, queue);
        } catch (Exception ex) {
            LOGGER.error("An exception has been thrown during receiving message", ex);
            ex.printStackTrace();
        }
        return false;
    }

    @Nullable
    @Override
    protected MessageDto editItem(MessageDto o) {
        return null;
    }

    public static class MyTableModel extends TableModel<MessageDto> {
        private static final String[] COLUMNS = {"ID", "Timestamp", "Type", "Delivery mode", "Priority", "Expiration", "Payload"};
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Nullable
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMNS[columnIndex];
        }

        @Override
        public Object getField(MessageDto dto, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return dto.getMessageID();
                case 1:
                    Date timestamp = new Date(dto.getTimestamp());
                    return FORMAT.format(timestamp);
                case 2:
                    return dto.getJmsType();
                case 3:
                    return dto.getDeliveryMode();
                case 4:
                    return dto.getPriority();
                case 5:
                    return dto.getExpiration();
                case 6:
                    return dto.getPayload();
            }
            return null;
        }
    }
}
