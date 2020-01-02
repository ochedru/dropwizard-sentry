package org.dhatim.dropwizard.sentry.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;

import java.util.List;

/**
 * Delegate appender: enables redefining the behavior of an existing appender.
 */
public class DelegateAppender implements Appender<ILoggingEvent> {

    private final Appender<ILoggingEvent> appender;

    public DelegateAppender(Appender<ILoggingEvent> appender) {
        this.appender = appender;
    }

    @Override
    public String getName() {
        return appender.getName();
    }

    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        appender.doAppend(event);
    }

    @Override
    public void setName(String name) {
        appender.setName(name);
    }

    @Override
    public void start() {
        appender.start();
    }

    @Override
    public void stop() {
        appender.stop();
    }

    @Override
    public boolean isStarted() {
        return appender.isStarted();
    }

    @Override
    public void setContext(Context context) {
        appender.setContext(context);
    }

    @Override
    public Context getContext() {
        return appender.getContext();
    }

    @Override
    public void addStatus(Status status) {
        appender.addStatus(status);
    }

    @Override
    public void addInfo(String msg) {
        appender.addInfo(msg);
    }

    @Override
    public void addInfo(String msg, Throwable ex) {
        appender.addInfo(msg, ex);
    }

    @Override
    public void addWarn(String msg) {
        appender.addWarn(msg);
    }

    @Override
    public void addWarn(String msg, Throwable ex) {
        appender.addWarn(msg, ex);
    }

    @Override
    public void addError(String msg) {
        appender.addError(msg);
    }

    @Override
    public void addError(String msg, Throwable ex) {
        appender.addError(msg, ex);
    }

    @Override
    public void addFilter(Filter<ILoggingEvent> newFilter) {
        appender.addFilter(newFilter);
    }

    @Override
    public void clearAllFilters() {
        appender.clearAllFilters();
    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        return appender.getCopyOfAttachedFiltersList();
    }

    @Override
    public FilterReply getFilterChainDecision(ILoggingEvent event) {
        return appender.getFilterChainDecision(event);
    }
}
