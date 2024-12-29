FROM mcr.microsoft.com/playwright/java:v1.49.0-noble

COPY src sharechat/src
COPY testNGFiles sharechat/testNGFiles
COPY TestData sharechat/TestData
COPY config config/TestData
COPY pom.xml sharechat/pom.xml

WORKDIR sharechat

CMD ["mvn", "clean", "test", "-DsuiteXmlFile=testNGFiles/tests.xml"]
