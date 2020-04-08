# FROM davimss/tomcat:9.0.33-slim
FROM davimss/tomcat-alpine:9.0.33

ENV WAR_FILE ./target/springcrud.war

# RUN curl -o $CATALINA_HOME/webapps/$WAR_FILE -L https://www.dropbox.com/s/4y0176vw0h7zns1/${WAR_FILE}?dl=1

COPY ${WAR_FILE} $CATALINA_HOME/webapps/

EXPOSE 8443