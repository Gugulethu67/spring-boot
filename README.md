# Vacation Management System

A comprehensive REST API for managing employee vacation requests built with Spring Boot. This system allows employees to submit vacation requests and managers to review, approve, or reject them while tracking vacation day balances.


## Features

### Employee Features
- View personal vacation requests
- Filter requests by status (pending, approved, rejected)
- Check remaining vacation days balance
- Submit new vacation requests with validation
- Automatic validation against available vacation days

### Manager Features
- View all vacation requests across the organization
- Filter requests by status or multiple statuses
- View comprehensive employee overviews
- Identify overlapping vacation requests
- Approve or reject vacation requests
- Generate summary statistics and reports
- Filter requests by date ranges


## Technology Stack
- **Java 24**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2/PostgreSQL Database**
- **Maven**
- **JUnit 5 & Mockito** (for testing)
- **springdoc-openapi** (for Swagger API documentation)


## Prerequisites
- **Java 24**
- **Maven 3.6+**
- IDE such as **IntelliJ IDEA**, **Eclipse**, or **VS Code**


## API Documentation
Once the application is running, you can access the Swagger UI:

- **Swagger UI**:  
http://localhost:8080/swagger-ui.html


## API Endpoints

### **Employee Endpoints**

**Get Employee Vacation Requests**  

GET /api/vacation-requests/employee/{employeeId}


**Filter Requests by Status**  


GET /api/vacation-requests/employee/{employeeId}/status/{status}

Statuses: `pending`, `approved`, `rejected`

**Get Remaining Vacation Days**  


GET /api/vacation-requests/employee/{employeeId}/remaining-days


**Create Vacation Request**  

POST /api/vacation-requests
Content-Type: application/json

{
  "author": 1,
  "vacationStartDate": "2025-09-01T00:00:00",
  "vacationEndDate": "2025-09-05T00:00:00"
}


### Manager Endpoints


**Get All Vacation Requests**

GET /api/vacation-requests



**Filter by Status**

GET /api/vacation-requests/status/{status}
GET /api/vacation-requests/status?statuses=pending,approved



**Employee Overview**

GET /api/vacation-requests/employee/{employeeId}/overview



**Overlapping Requests**

GET /api/vacation-requests/overlapping



**Date Range Filter**

GET /api/vacation-requests/date-range?start=2025-01-01&end=2025-12-31



**Summary Statistics**

GET /api/vacation-requests/summary


Approve/Reject Request
**Approve/Reject Request**

PUT /api/vacation-requests/{requestId}/status
Content-Type: application/json

{
  "status": "approved",
  "managerId": 2
}


### Employee Management Endpoints



**Get All Employees**

GET /api/employees



**Create Employee**

POST /api/employees
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "isManager": false,
  "totalVacationDays": 25
}
