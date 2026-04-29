FROM ingsof-image AS build 
ENV PATH_JAVA_FX=/app/modules/javafx-sdk-21.0.11/lib
ENV MODULES=javafx.controls
ENV BUILD=/app/build

RUN mkdir /app/src
RUN mkdir /app/modules
WORKDIR /app
RUN --mount=type=bind,src=src,target=/app/src \
	   --mount=type=bind,src=modules,target=/app/modules \
	   javac -d ${BUILD} -p ${PATH_JAVA_FX} --add-modules ${MODULES} src/java/HelloWorld.java

CMD java -cp ${BUILD}  -p ${PATH_JAVA_FX} --add-modules ${MODULES} HelloWorld
