# UX Analytics Tool

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Description](#description)
- [Main Components](#main-components)
    - [Chrome plugin](#chrome-plugin)
    - [Backend server component](#backend-server-component)

## Description

This UX Analytics Tool is a master's thesis project designed to provide a comprehensive analysis of user experience (UX). It leverages biometric and behavioristic data, including scroll patterns, mouse movements, and keyboard
interactions, to gain insights into user behavior on the screen. The goal of this tool is to enhance the understanding
of user interactions and improve UX design based on data-driven insights.

## Main Components

The UX Analytics Tool consists of three main components: a Chrome plugin and a backend server.

### Chrome plugin

The Chrome Plugin component is a JavaScript-based extension for the Chrome browser. It is responsible for capturing the
user's biometric and behavioristic data while they interact with a webpage. This data includes scroll patterns, mouse
movements, and keyboard interactions. The captured data is then sent to the backend component for further processing and
analysis. The goal of this plugin is to provide a seamless way of collecting user interaction data without disrupting
the user's browsing experience.

### Backend server component

The backend component, named ux-analytics-backend, is a Java-based application built with the Spring Boot framework. It
is responsible for processing and analyzing the biometric and behavioristic data collected from the user interactions.
This includes scroll patterns, mouse movements, and keyboard interactions. The backend uses MongoDB as its database,
storing and retrieving data as needed. It also includes various dependencies for testing, such as Testcontainers, and
for code simplification, such as Lombok. The backend is built with Maven, which handles the project's dependency
management and build lifecycle.

## Installation and usage

### Backend server

1. Clone the repository to your local machine.
2. Open the terminal and navigate to the `ux-analytics-backend` folder.
3. Navigate to the `src/main/docker` folder and run the command `docker-compose up` to start the MongoDB database.
4. Navigate back to the `ux-analytics-backend` folder.
5. Run the command `mvn clean install` to build the project.
6. Run the command `mvn spring-boot:run` to start the backend server.
7. The server should now be running on `http://localhost:8080`.

### Chrome plugin

The Chrome plugin is not yet available on the Chrome Web Store. To install it, follow these steps:

1. Clone the repository to your local machine.
2. Open the Chrome browser and navigate to `chrome://extensions`.
3. Enable the "Developer mode" toggle in the top right corner.
4. Click the "Load unpacked" button in the top left corner.
5. Select the `chrome-plugin` folder from the cloned repository.
6. The plugin should now be installed and ready to use.

### Testing the plugin

To test it, navigate to any webpage and click the plugin icon in the top right corner of the browser.
Prerequisite is to have the backend server running.

1. Navigate to any webpage and click the plugin icon in the top right corner of the browser.
2. To start capturing data, click the "Start" button in the popup window.
3. To stop capturing data, click the "Stop" button in the popup window.

The captured data is now sent to the backend server.

To view the captured data, you can open the MongoDB database through the MongoDB Express interface which is run through
the docker-compose, with the database. The MongoDB Express interface is available on `http://localhost:8111`.