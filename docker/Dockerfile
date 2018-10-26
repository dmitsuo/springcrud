FROM davimss/tomcat:9.0.10

ENV WAR_FILE springcrud.war

RUN curl -o $CATALINA_HOME/webapps/$WAR_FILE -L https://www.dropbox.com/s/4y0176vw0h7zns1/${WAR_FILE}?dl=1

EXPOSE 8443