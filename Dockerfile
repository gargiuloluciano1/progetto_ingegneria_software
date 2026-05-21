FROM ingsof-image AS build 
ENV PATH_JAVA_FX=/app/modules/javafx-sdk-21.0.11/lib
ENV MODULES=javafx.controls
ENV BUILD=/app/build
ENV PATH_SRC=/app/src/java

RUN mkdir /app/src
RUN mkdir /app/modules
WORKDIR /app

RUN --mount=type=bind,src=src,target=/app/src \
	   --mount=type=bind,src=modules,target=/app/modules \
	   javac -d ${BUILD} -p ${PATH_JAVA_FX} --add-modules ${MODULES} src/java/*/*.java src/java/*.java



CMD java -cp ${BUILD}  -p ${PATH_JAVA_FX} --add-modules ${MODULES} HelloWorld
