package org.dhatim.dropwizard.sentry.logging;

import ch.qos.logback.classic.Logger;
import io.dropwizard.logging.async.AsyncLoggingEventAppenderFactory;
import io.dropwizard.logging.filter.ThresholdLevelFilterFactory;
import io.dropwizard.logging.layout.DropwizardLayoutFactory;
import java.util.Optional;
import java.util.Set;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import org.slf4j.LoggerFactory;

/**
 * A class adding a configured
 * {@link io.sentry.logback.SentryAppender} to the root logger.
 */
public final class SentryBootstrap {

    private SentryBootstrap() {
        /* No instance methods */ }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn The DSN (Data Source Name) for your project
     */
    public static void bootstrap(final String dsn) {
        bootstrap(dsn, true);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn The DSN (Data Source Name) for your project
     * @param cleanRootLogger If true, detach and stop all other appenders from
     * the root logger
     */
    public static void bootstrap(final String dsn, boolean cleanRootLogger) {
        bootstrap(dsn, Optional.empty(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn The DSN (Data Source Name) for your project
     * @param mdcTags Tag names to be extracted from logging MDC
     * @param cleanRootLogger If true, detach and stop all other appenders from
     * the root logger
     */
    public static void bootstrap(final String dsn, Optional<Set<String>> mdcTags, boolean cleanRootLogger) {
        bootstrap(dsn, mdcTags, Optional.empty(), Optional.empty(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn The DSN (Data Source Name) for your project
     * @param mdcTags Tag names to be extracted from logging MDC
     * @param environment The environment name to pass to Sentry
     * @param release The release name to pass to Sentry
     * @param cleanRootLogger If true, detach and stop all other appenders from
     * the root logger
     */
    public static void bootstrap(final String dsn, Optional<Set<String>> mdcTags,
                                 Optional<String> environment, Optional<String> release, boolean cleanRootLogger) {
        bootstrap(dsn, mdcTags, environment, release, Optional.empty(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn The DSN (Data Source Name) for your project
     * @param mdcTags Tag names to be extracted from logging MDC
     * @param environment The environment name to pass to Sentry
     * @param release The release name to pass to Sentry
     * @param serverName The server name to pass to Sentry
     * @param cleanRootLogger If true, detach and stop all other appenders from
     * the root logger
     */
    public static void bootstrap(final String dsn, Optional<Set<String>> mdcTags,
                                 Optional<String> environment, Optional<String> release, Optional<String> serverName,
                                 boolean cleanRootLogger) {
        bootstrap(dsn, Optional.empty(), mdcTags, environment, release, Optional.empty(), cleanRootLogger);
    }

    /**
     * Bootstrap the SLF4J root logger with a configured
     * {@link io.sentry.logback.SentryAppender}.
     *
     * @param dsn             The DSN (Data Source Name) for your project
     * @param thresholdOptional log events threshold
     * @param mdcTags         Tag names to be extracted from logging MDC
     * @param environment     The environment name to pass to Sentry
     * @param release         The release name to pass to Sentry
     * @param serverName      The server name to pass to Sentry
     * @param cleanRootLogger If true, detach and stop all other appenders from
     *                        the root logger
     */
    public static void bootstrap(final String dsn, Optional<String> thresholdOptional, Optional<Set<String>> mdcTags,
                                 Optional<String> environment, Optional<String> release, Optional<String> serverName,
                                 boolean cleanRootLogger) {
        final SentryAppenderFactory factory = new SentryAppenderFactory();
        factory.setDsn(dsn);
        factory.setMdcTags(mdcTags);
        factory.setEnvironment(environment);
        factory.setRelease(release);
        factory.setServerName(serverName);
        thresholdOptional.ifPresent(t -> factory.setThreshold(t));

        registerAppender(dsn, cleanRootLogger, factory);
    }

    private static void registerAppender(String dsn, boolean cleanRootLogger,
                                         SentryAppenderFactory factory) {
        final Logger root = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);

        if (cleanRootLogger) {
            root.detachAndStopAllAppenders();
        }

        final ThresholdLevelFilterFactory levelFilterFactory = new ThresholdLevelFilterFactory();
        final DropwizardLayoutFactory layoutFactory = new DropwizardLayoutFactory();
        final AsyncLoggingEventAppenderFactory asyncAppenderFactory
                = new AsyncLoggingEventAppenderFactory();
        root.addAppender(factory.build(root.getLoggerContext(), dsn, layoutFactory, levelFilterFactory,
                asyncAppenderFactory));
    }

    public static class Builder {


        private final String dsn;
        private Optional<String> thresholdOptional = Optional.empty();
        private Optional<Set<String>> mdcTags = Optional.empty();
        private Optional<String> environment = Optional.empty();
        private Optional<String> release = Optional.empty();
        private Optional<String> serverName = Optional.empty();
        private boolean cleanRootLogger;

        public static Builder withDsn(String dsn) {
            Builder builder = new Builder(dsn);
            return builder;
        }

        /**
         *
         * @param dsn The DSN (Data Source Name) for your project
         */
        private Builder(String dsn) {
            this.dsn = dsn;
        }

        /**
         *
         * @param threshold log events threshold
         * @return
         */
        public Builder withThreshold(String threshold) {
            this.thresholdOptional = Optional.of(threshold);
            return this;
        }

        /**
         *
         * @param mdcTags Tag names to be extracted from logging MDC
         * @return
         */
        public Builder withMdcTags(Set<String> mdcTags) {
            this.mdcTags = Optional.of(mdcTags);
            return this;
        }

        /**
         *
         * @param environment The environment name to pass to Sentry
         * @return
         */
        public Builder withEnvironment(String environment) {
            this.environment = Optional.of(environment);
            return this;
        }

        /**
         *
         * @param release The release name to pass to Sentry
         * @return
         */
        public Builder withRelease(String release) {
            this.release = Optional.of(release);
            return this;
        }

        /**
         *
         * @param serverName The server name to pass to Sentry
         * @return
         */
        public Builder withServerName(String serverName) {
            this.serverName = Optional.of(serverName);
            return this;
        }

        /**
         *
         * @param cleanRootLogger If true, detach and stop all other appenders from
         *                        the root logger
         * @return
         */
        public Builder withCleanRootLogger(boolean cleanRootLogger) {
            this.cleanRootLogger = cleanRootLogger;
            return this;
        }

        /**
         * Bootstrap the SLF4J root logger with a configured
         *       {@link io.sentry.logback.SentryAppender}.
         */
        public void bootstrap() {
            SentryBootstrap.bootstrap(dsn, thresholdOptional, mdcTags, environment, release, serverName, cleanRootLogger);
        }
    }
}
