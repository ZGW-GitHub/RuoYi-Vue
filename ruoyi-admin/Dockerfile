FROM eclipse-temurin:17-jre as builder
WORKDIR extracted
COPY ./target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


FROM eclipse-temurin:17-jdk
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

# arthas
COPY --from=hengyunabc/arthas:3.7.2-no-jdk /opt/arthas ./tool/arthas

ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
ENV PATH_LOG="/application/log"
ENV PATH_TEMP="/application/tmp"
ENV JAVA_OPTS="-Xms32m -Xmx128m -Xmn40m"
ENV JAVA_JFR_OPTS="-XX:StartFlightRecording=settings=default,delay=10s,name=life-record,dumponexit=false,disk=true,maxsize=1024m,maxage=3d \
-XX:FlightRecorderOptions=maxchunksize=128m,repository=$PATH_TEMP/jfr"
ENV JAVA_LOG_OPTS="-Xlog:async -XX:AsyncLogBufferSize=4194304 \
-Xlog:gc*=debug:file=$PATH_LOG/jvm/gc.%p.%t.log:utctime,uptime,level,tags:filecount=50,filesize=100M \
-Xlog:jit+compilation=info:file=$PATH_LOG/jvm/jit-compile.%p.%t.log:utctime,uptime,level,tags:filecount=10,filesize=10M \
-Xlog:safepoint=debug:file=$PATH_LOG/jvm/safepoint.%p.%t.log:utctime,uptime,level,tags:filecount=10,filesize=10M"
#ENV DUBBO_IP_TO_REGISTRY=124.223.3.202
#ENV DUBBO_IP_TO_BIND=10.0.12.15

RUN mkdir -p "$PATH_LOG/jvm"
RUN mkdir -p "$PATH_TEMP/jfr"

EXPOSE 54321

# 在 springboot 3.2 之前使用:
ENTRYPOINT java ${JAVA_OPTS} ${JAVA_LOG_OPTS} ${JAVA_JFR_OPTS} org.springframework.boot.loader.JarLauncher
# 在 springboot 3.2 之后使用:
#ENTRYPOINT java ${JAVA_OPTS} ${JAVA_LOG_OPTS} ${JAVA_JFR_OPTS} org.springframework.boot.loader.launch.JarLauncher
