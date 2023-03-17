ARG SSL_SUPPORT_VER=1.3.0
ARG NEW_RELIC_VER=7.4.2
ARG BASE_IMAGE=aetna-java:11-distroless

FROM docker.nexus.aetnadigital.net/hccisvc/ssl-support:${SSL_SUPPORT_VER} as ssl-support
FROM docker.nexus.aetnadigital.net/hccisvc/newrelic-java-agent:${NEW_RELIC_VER} AS newrelic-agent
FROM docker.nexus.aetnadigital.net/${BASE_IMAGE}

ARG jarPath
ARG jarName

ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
ENV _JAVA_OPTIONS="-Djavax.net.ssl.trustStore=/app/keystore.jks"
ENV JDBC_PARAMS="ssl=true"

COPY --from=ssl-support --chown="aetna:aetna" /app/rds.jks /app/keystore.jks
COPY --from=ssl-support --chown="aetna:aetna" /app/rds.pem /app/rds.pem
COPY --from=newrelic-agent --chown="aetna:aetna" /usr/local/newrelic/ /app/
COPY --chown="aetna:aetna" $jarPath/$jarName /app/app.jar

CMD ["java", "-javaagent:/app/newrelic-agent.jar", "-jar", "/app/app.jar" ]

HEALTHCHECK NONE
