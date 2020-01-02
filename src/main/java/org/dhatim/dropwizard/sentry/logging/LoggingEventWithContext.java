package org.dhatim.dropwizard.sentry.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import io.sentry.Sentry;
import io.sentry.context.Context;
import io.sentry.event.Breadcrumb;
import io.sentry.event.User;
import io.sentry.event.interfaces.HttpInterface;
import org.slf4j.Marker;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Delegate logging event that saves the current Sentry context upon construction.
 */
public class LoggingEventWithContext implements ILoggingEvent {

    private final ILoggingEvent event;
    final User user;
    final HttpInterface http;
    final List<Breadcrumb> breadcrumbs;
    final UUID lastEventId;
    final Map<String, Object> extra;
    final Map<String, String> tags;

    public LoggingEventWithContext(ILoggingEvent event) {
        this.event = event;
        Context context = Sentry.getContext();
        this.user = context.getUser();
        this.http = context.getHttp();
        this.breadcrumbs = context.getBreadcrumbs();
        this.lastEventId = context.getLastEventId();
        this.extra = context.getExtra();
        this.tags = context.getTags();
    }

    @Override
    public String getThreadName() {
        return event.getThreadName();
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    @Override
    public String getMessage() {
        return event.getMessage();
    }

    @Override
    public Object[] getArgumentArray() {
        return event.getArgumentArray();
    }

    @Override
    public String getFormattedMessage() {
        return event.getFormattedMessage();
    }

    @Override
    public String getLoggerName() {
        return event.getLoggerName();
    }

    @Override
    public LoggerContextVO getLoggerContextVO() {
        return event.getLoggerContextVO();
    }

    @Override
    public IThrowableProxy getThrowableProxy() {
        return event.getThrowableProxy();
    }

    @Override
    public StackTraceElement[] getCallerData() {
        return event.getCallerData();
    }

    @Override
    public boolean hasCallerData() {
        return event.hasCallerData();
    }

    @Override
    public Marker getMarker() {
        return event.getMarker();
    }

    @Override
    public Map<String, String> getMDCPropertyMap() {
        return event.getMDCPropertyMap();
    }

    @Override
    public Map<String, String> getMdc() {
        return this.getMDCPropertyMap();
    }

    @Override
    public long getTimeStamp() {
        return event.getTimeStamp();
    }

    @Override
    public void prepareForDeferredProcessing() {
        event.prepareForDeferredProcessing();
    }
}
