FROM ubuntu:22.04

RUN apt-get update && apt-get install -y dotnet-sdk-8.0

#RUN dotnet new install Avalonia.Templates
#RUN dotnet new avalonia.app -o GetStartedApp
