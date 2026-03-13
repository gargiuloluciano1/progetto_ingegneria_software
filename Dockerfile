FROM ubuntu:22.04

# TODO 
# need to setup ssh daemon on docker, enable x11 forwarding and start application
RUN apt-get update && apt-get install -y dotnet-sdk-8.0
RUN apt-get install -y x11-apps

WORKDIR /app
ENTRYPOINT ["dotnet", "run"]
