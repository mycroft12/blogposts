FROM openjdk:11

RUN echo -e "*** Deploy Blog Posts Backend ***"

COPY target/blogposts*.jar /blogposts.jar

ENTRYPOINT ["java", "-jar", "blogposts.jar"]