# Dropwizard Sentry

[![Build Status](https://travis-ci.org/dhatim/dropwizard-sentry.png?branch=master)](https://travis-ci.org/dhatim/dropwizard-sentry)
[![Coverage Status](https://coveralls.io/repos/github/dhatim/dropwizard-sentry/badge.svg?branch=master)](https://coveralls.io/github/dhatim/dropwizard-sentry?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-sentry/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-sentry)
[![Javadocs](https://www.javadoc.io/badge/org.dhatim/dropwizard-sentry.svg)](https://www.javadoc.io/doc/org.dhatim/dropwizard-sentry)

Dropwizard integration for error logging to [Sentry](https://sentry.io).

## Usage

Dropwizard Sentry provides an `AppenderFactory` which is automatically registered in Dropwizard and will send errors to Sentry.

### Logging startup errors

In order to log startup errors (i.e. before the `SentryAppenderFactory` has been properly initialized), the Dropwizard application has to run the `SentryBootstrap.bootstrap()` in its `main` method and set a custom `UncaughtExceptionHandler` for the main thread.

```java
public static void main(String[] args) throws Exception {
    SentryBootstrap.bootstrap(DSN);
    Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());

    new MyDropwizardApplication().run(args);
}
```

Please note that by default startup logger will catch all logs, you can specify custom threshold as following:

```java
public static void main(String[] args) throws Exception {
    SentryBootstrap.Builder()
        .withDsn(DSN)
        .withThreshold(THRESHOLD)
        .bootstrap();
    Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());

    new MyDropwizardApplication().run(args);
}
```

### Configuration

Include the `sentry` appender in your application's YAML configuration:

```yaml
appenders:
  - type: sentry
    threshold: ERROR
    dsn: https://user:pass@sentry.io/appid
    environment: production
    mdcTags: ['foo','bar','baz']
    sentryClientFactory: com.example.SentryClientFactory
    release: 1.0.0
    serverName: 10.0.0.1
    extra: {key1:'value1',key2:'value2'}
    stacktraceAppPackages: ['com.example','com.foo']
```

| Setting | Default | Description | Example Value |
|---|---|---|---|
| `threshold` | ALL | The log level to configure to send to Sentry | `ERROR` |
| `dsn` |   | Data Source Name - format is `https://{PUBLIC_KEY}:{SECRET_KEY}@sentry.io/{PROJECT_ID}` | `https://foo:bar@sentry.io/12345` |
| `environment` | [empty] | The environment your application is running in |  `production` |
| `tags` | [empty] | Tags to be sent with each event | `tag1:value1,tag2,value2` |
| `mdcTags` | [empty] | Tag names to be extracted from logging MDC | `['foo', 'bar']` |
| `sentryClientFactory` | [empty] | Specify a custom [`SentryClientFactory`](https://github.com/getsentry/sentry-java/blob/master/sentry/src/main/java/io/sentry/SentryClientFactory.java) class | `com.example.SentryClientFactory` |
| `release` | [empty] | The release version of your application | `1.0.0` |
| `serverName` | [empty] | Override the server name (rather than looking it up dynamically) | `10.0.0.1` |
| `extra` | [empty] | Extra data to be sent with errors (but not as tags) | `{key1:'value1',key2:'value2'}` |
| `stacktraceAppPackages` | [empty] | List of package prefixes used by application code | `['com.example','com.foo']` |

If you need to set configuration properties not listed above, append them to the `dsn` as described [here](https://docs.sentry.io/clients/java/config/#configuration-via-the-dsn).

## Maven Artifacts

This project is available in the [Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.dhatim%22%20AND%20a%3A%22dropwizard-sentry%22). To add it to your project simply add the following dependency to your POM:

```xml
<dependency>
  <groupId>org.dhatim</groupId>
  <artifactId>dropwizard-sentry</artifactId>
  <version>1.3.1-1</version>
</dependency>
```

## Support

Please file bug reports and feature requests in [GitHub issues](https://github.com/dhatim/dropwizard-sentry/issues).

## Acknowledgements

Thanks to [dropwizard-raven](https://github.com/tradier/dropwizard-raven) from which much of the original implementation was derived.
