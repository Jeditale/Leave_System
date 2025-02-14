# Project Setup Guide

This guide will walk you through setting up and running both the backend and frontend applications along with `docker-compose` for a smooth development workflow.

## Before you begin, ensure you have the following installed:

- **Docker** and **Docker Compose**: To run both the frontend and backend containers.
- **Node.js**: To run the frontend if you're not using Docker.
- **Java**: For the backend (Spring Boot).
- **Maven**: To build the Spring Boot backend application.

## Project Structure

- `LeaveSystemBack/` — The backend folder that includes the Spring Boot application.
- `leave-system-frontend/` — The frontend folder that includes the Angular application.

## Setting up Backend (Spring Boot)

1. **Navigate to the backend directory**:
```
   cd LeaveSystemBack
```
2.Build the backend project (If you haven't built it yet):
```
mvn clean install
```
3.Compose the server:
```
docker-compose up -d
```
4.Run the the app
```
mvn spring-boot:run
```

## Setting up Frontend (Angular)

1.Navigate to the frontend directory:
```
cd leave-system-frontend
```
2.Install the dependencies:
```
npm install
```
3.Run app
```
ng serve
```

