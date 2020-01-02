package org.dhatim.dropwizard.sentry.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.sentry.Sentry;
import io.sentry.context.Context;
import io.sentry.event.EventBuilder;
import io.sentry.logback.SentryAppender;

/**
 * Sentry appender that sets the current Sentry context from the logging event if it is of type
 * {@link LoggingEventWithContext}.
 */
public class SentryAppenderEx extends SentryAppender {

    @Override
    protected EventBuilder createEventBuilder(ILoggingEvent event) {
        Context context = Sentry.getContext();
        context.clear();
        if (event instanceof LoggingEventWithContext) {
            LoggingEventWithContext e = (LoggingEventWithContext) event;
            context.setUser(e.user);
            context.setHttp(e.http);
            context.setLastEventId(e.lastEventId);
            e.breadcrumbs.forEach(context::recordBreadcrumb);
            e.tags.forEach(context::addTag);
            e.extra.forEach(context::addExtra);
        }
        return super.createEventBuilder(event);
    }

}
