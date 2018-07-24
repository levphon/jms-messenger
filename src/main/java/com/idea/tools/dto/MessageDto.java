package com.idea.tools.dto;

import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@ToString(exclude = {"queue", "destination", "headers"})
@EqualsAndHashCode(exclude = {"queue", "destination", "headers"})
public class MessageDto implements Comparable<MessageDto> {

    private static final Comparator<Long> COMPARATOR = Comparator.nullsLast(Comparator.reverseOrder());

    private String messageID;
    private String correlationId;
    private Long timestamp;
    private String jmsType;
    private ContentType type;
    private String payload;
    private Integer deliveryMode;
    private Integer priority;
    private Long expiration;
    @Deprecated
    private QueueDto queue;
    private DestinationDto destination;
    private List<HeaderDto> headers = new ArrayList<>();

    @Transient
    public QueueDto getQueue() {
        return queue;
    }

    @Transient
    public DestinationDto getDestination() {
        return destination;
    }

    @Override
    public int compareTo(@NotNull MessageDto o) {
        return COMPARATOR.compare(timestamp, o.timestamp);
    }
}
