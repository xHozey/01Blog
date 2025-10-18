# 01Blog - Social Blogging Platform

A fullstack social blogging platform where students can share their learning experiences, follow other learners, and engage in meaningful discussions about their educational journey.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)

## 🎯 Overview

01Blog is a comprehensive social blogging platform designed specifically for students to document and share their learning journey. The platform enables users to create rich content with media, interact through likes and comments, follow other learners, and maintain a safe community through reporting and moderation features.

## ✨ Features

### User Features

- **Authentication & Authorization**: Secure user registration and login with role-based access control
- **Personal Block Page**: Each user has a public profile displaying all their posts
- **Post Management**: Create, edit, and delete posts with text and media (images/videos)
- **Social Interactions**: Like and comment on posts from other users
- **Subscriptions**: Follow other users and receive notifications for new posts
- **Notifications**: Stay updated with activity from subscribed profiles
- **Reporting System**: Report inappropriate content or user behavior

### Admin Features

- **User Management**: View and manage all registered users
- **Content Moderation**: Review and remove inappropriate posts
- **Report Handling**: Process user reports and take appropriate actions (ban users, delete content)
- **Dashboard**: Comprehensive overview of platform activity

## 🛠️ Technologies Used

### Backend

- **Java 17+** - Programming language
- **Spring Boot** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access and ORM
- **JWT** - Stateless authentication
- **PostgreSQL** - Relational database
- **Maven** - Dependency management and build tool
- **Hibernate** - ORM implementation
- **Lombok** - Reduce boilerplate code

### Frontend

- **Angular** - Frontend framework
- **TypeScript** - Programming language
- **Bootstrap** - UI component library
- **RxJS** - Reactive programming
- **Angular Router** - Navigation and routing
- **HttpClient** - API communication
- **Angular Forms** - Form handling and validation

### Development Tools

- **Git** - Version control
- **Postman** - API testing
- **VS Code** - IDE

## 📦 Prerequisites

Before running the application, ensure you have the following installed:

- **Java JDK 17 or higher**
- **Node.js 18+ and npm**
- **Docker** (for PostgreSQL)
- **Maven 3.8+**
- **Angular CLI** - Install via: `npm install -g @angular/cli`
- **Git**

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/xHozey/01Blog
cd 01Blog
```

### 2. Database Setup

Pull and run PostgreSQL using Docker:

```bash
docker pull postgres
docker run --name blog-postgres -e POSTGRES_PASSWORD=supersecret -p 5432:5432 -d postgres
```

This will start a PostgreSQL instance on port 5432 with password `supersecret`.

## 🏃 Running the Application

### Backend (Spring Boot)

Navigate to the backend directory and run:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend API will be available at **http://localhost:8080**

**Alternative - Run from IDE:**

- Open the project in IntelliJ IDEA or Eclipse
- Locate the main application class (e.g., `BlogApplication.java`)
- Run the application

### Frontend (Angular)

Navigate to the frontend directory and run:

```bash
cd frontend

# Install dependencies
npm install

# Start the development server
ng serve
```

The frontend will be available at **http://localhost:4200**

**Production Build:**

```bash
ng build --configuration production
```

Build artifacts will be stored in the `dist/` directory.

## 🔧 Troubleshooting

### Common Issues

**Backend won't start:**

- Check if PostgreSQL Docker container is running: `docker ps`
- Verify database credentials match your configuration
- Ensure port 8080 is not in use

**Frontend won't compile:**

- Delete `node_modules` and run `npm install` again
- Clear npm cache: `npm cache clean --force`
- Verify Node.js version compatibility

**CORS errors:**

- Verify CORS configuration in backend security config
- Check that frontend is making requests to correct API URL

**File upload fails:**

- Check file size limits in backend configuration
