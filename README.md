# Project Tracking Management System (PTMS)

## 1. Project Overview

Project Tracking Management System (PTMS) is a console-based Java application used to manage projects, project members, tickets, and ticket progress.

The system provides role-based access so that different users can perform operations according to their responsibilities.

The application uses Core Java, JDBC and MySQL with a layered architecture.

---

## 2. Main Features

### Authentication
- User login using email and password.
- Role-based dashboard after successful login.

### User Management
Admin can:
- Create users
- View a user
- View all users
- Update users
- Delete users
- Find users by role

### Project Management
Authorized users can:
- Create projects
- View projects
- Update projects
- Delete projects
- Search projects by domain

Project information includes:
- Project name
- Requirements
- Manager
- Team Lead
- Client
- Domain
- Cost
- Start date
- Deadline
- Priority
- Status

### Project Member Management
The system supports:
- Adding project members
- Removing project members
- Checking project membership
- Preventing duplicate project membership

### Ticket Management
Team Leads can:
- Create tickets
- View tickets
- View all tickets
- Update tickets
- Assign tickets
- Delete tickets
- Filter tickets by status
- Filter tickets by priority
- View tickets belonging to a project

### Ticket Tracking
Employees and Team Leads can:
- Update ticket progress
- Add comments
- Update ticket status
- View ticket tracking history
- View their own updates

The system validates ticket progress between 0 and 100.

A ticket marked as COMPLETED must have 100% progress.

---

## 3. User Roles

The application currently supports four roles:

### ADMIN
- Manage users
- Manage projects
- Delete projects

### PROJECT_MANAGER
- Create projects
- View projects
- Update projects
- Search projects

### TEAM_LEAD
- Manage project members
- Create and manage tickets
- Assign tickets
- Review ticket tracking

### EMPLOYEE
- View and update tracking for tickets assigned to them

Role authorization is also checked in the service layer.

---

## 4. Technology Stack

- Java
- Maven
- JDBC
- MySQL
- JUnit 5
- Mockito
- IntelliJ IDEA
- Git and GitHub

---

## 5. Architecture

The application follows a layered architecture:

```text
MainApp
   |
   v
Controller
   |
   v
Service
   |
   v
DAO
   |
   v
JDBC / DBConnection
   |
   v
MySQL