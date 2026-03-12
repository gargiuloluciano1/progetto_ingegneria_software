FROM ubuntu:22.04

# TODO 
# need to setup ssh daemon on docker, enable x11 forwarding and start application
RUN apt-get update && apt-get install -y vim openssh-server  dotnet-sdk-8.0

RUN dotnet new install Avalonia.Templates
RUN dotnet new avalonia.app -o GetStartedApp
