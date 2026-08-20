FROM ingsof-image AS build
ARG JAVAFX_VERSION=21.0.12
ARG JAVAFX_ARCH=linux-x64
WORKDIR /app
RUN apt-get update && apt-get install -y wget unzip \
    && wget --no-verbose --show-progress https://download2.gluonhq.com/openjfx/21.0.12/openjfx-21.0.12_linux-x64_bin-sdk.zip -O /tmp/javafx-sdk.zip \
    && ls -la /tmp/javafx-sdk.zip \
    && unzip -q /tmp/javafx-sdk.zip -d /opt \
    && rm /tmp/javafx-sdk.zip \
    && apt-get purge -y wget unzip && rm -rf /var/lib/apt/lists/*
ENV PATH_JAVA_FX=/opt/javafx-sdk-${JAVAFX_VERSION}/lib
ENV MODULES=javafx.controls
ENV BUILD=/app/build
ENV PATH_SRC=/app/src/java
# Classe di avvio del progetto (package completo, non piu' "HelloWorld")
ENV MAIN_CLASS=it.unibo.skiscope.Main
RUN mkdir -p /app/src
RUN mkdir -p /app/modules
WORKDIR /app
RUN --mount=type=bind,src=src,target=/app/src \
	   --mount=type=bind,src=modules,target=/app/modules \
	   find src/java -name "*.java" > /tmp/sources.txt \
	   && javac -d ${BUILD} -p ${PATH_JAVA_FX} --add-modules ${MODULES} @/tmp/sources.txt
CMD java -cp ${BUILD} -p ${PATH_JAVA_FX} --add-modules ${MODULES} ${MAIN_CLASS}
