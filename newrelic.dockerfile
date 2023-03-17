# Replace Dockerfile with this file to enable NewRelic APM
# Also, you will need to add following configuration into deploy section in pipeline/values.yaml file
# deploy:
#   env:
#     NEW_RELIC_APP_NAME: <your-application-name>-{{git.env}}
#   secrets:
#     NEW_RELIC_LICENSE_KEY: /kv/health-cloud-revolutionary/{{git.env}}/newrelic/global/license-key
FROM docker.nexus.aetnadigital.net/hccisvc/ssl-support:1.1.1 as ssl-support
FROM docker.nexus.aetnadigital.net/hccisvc/newrelic-java-agent:6.5.1 AS newrelic-agent

FROM docker.nexus.aetnadigital.net/openjdk:11.0.4-jre-slim

ARG jarPath
ARG jarName
ENV jarName=$jarName
ADD $jarPath/$jarName $jarName
ARG options
ENV options=$options
ARG args
ENV args=$args

COPY --from=ssl-support /app/rds.jks /app/rds.jks
COPY --from=newrelic-agent /usr/local/newrelic/ /app/
ENV JDBC_PARAMS="ssl=true"
ENV _JAVA_OPTIONS="-Djavax.net.ssl.trustStore=/app/rds.jks"

# to avoid running as root
#
RUN \
    groupadd --gid 1000 appgroup && \
    useradd --uid 1000 --gid appgroup appuser && \
    chown -R appuser:appgroup /app

USER appuser

# env vars don't play well with array syntax
#
CMD java $options -javaagent:/app/newrelic-agent.jar -jar $jarName $args
