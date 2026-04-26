# Job Recruitment API

A RESTful backend API for a job recruitment platform, built with Spring Boot 4, Spring Security, Spring Data JPA, and MySQL. The project provides modules for user management, company management, job postings, skills, resumes, permissions, file upload, and email notifications for subscribers.

## 1. Project Goals

This project is designed to support the following core use cases:

- Manage users, roles, and endpoint-level permissions.
- Manage companies, job postings, and skills.
- Manage candidate resumes and job applications.
- Support login, registration, JWT access tokens, and refresh tokens.
- Upload and download resume files.
- Send job recommendation emails based on subscriber skills.
- Provide API documentation through Swagger/OpenAPI.

## 2. Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Web | Spring Web MVC |
| Security | Spring Security, OAuth2 Resource Server, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Validation | Jakarta Validation |
| Mail | Spring Mail |
| Template Engine | Thymeleaf |
| API Docs | springdoc-openapi |
| Dynamic Filtering | `com.turkraft.springfilter` |
| Build Tool | Maven Wrapper |
| Boilerplate Reduction | Lombok |

## 3. Overall Architecture

The project follows a standard layered architecture:

- `controller`: receives requests, validates input, and returns responses.
- `service`: handles business logic.
- `repository`: performs database access through Spring Data JPA.
- `domain`: JPA entities mapped to database tables.
- `dto.request` / `dto.response`: request and response models used by the API.
- `config`: security, CORS, Swagger, interceptors, static resources, and database initialization.
- `util`: JWT utilities, response wrapper, exception handling, enums, and shared annotations.

Typical request flow:

1. The client sends an API request.
2. Spring Security validates the JWT if the endpoint requires authentication.
3. `PermissionInterceptor` checks database-driven permissions for selected endpoint groups.
4. The controller receives the request and calls the service layer.
5. The service works with repositories and entities.
6. `FormatRestResponse` wraps the output into a standard JSON response structure.

## 4. Project Structure

```text
src/
  main/
    java/thanhanh/job_recruitment/
      config/
      controller/
      domain/
      dto/
        request/
        response/
      repository/
      service/
        impl/
      util/
    resources/
      application.properties
      templates/
  test/
    java/thanhanh/job_recruitment/
```

## 5. Main Features

- JWT-based authentication.
- Session renewal using refresh tokens stored in an `HttpOnly` cookie.
- Dynamic permission checks using `permissions` and `roles`.
- CRUD operations for company, job, skill, user, role, and permission.
- Resume creation, update, and listing.
- Subscriber registration and job recommendation emails by skill.
- Local file upload and download.
- Pagination and dynamic filtering for multiple list APIs.

## 6. Current Configuration

The following values are currently defined in `src/main/resources/application.properties`:

| Property | Current Value | Description |
|---|---|---|
| `server.port` | `8080` | API server port |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/jobhunter` | MySQL connection URL |
| `spring.datasource.username` | `root` | Database username |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto-update database schema |
| `jwt.base64-secret` | configured | Secret key used to sign JWTs |
| `jwt.access-token-validity-in-seconds` | `8640000` | Access token lifetime, about 100 days |
| `jwt.refresh-token-validity-in-seconds` | `8640000` | Refresh token lifetime, about 100 days |
| `upload-file.base-uri` | `file:///D:/JAVA-RESTFUL/upload/` | Root folder for uploaded files |
| `spring.servlet.multipart.max-file-size` | `50MB` | Maximum upload file size |
| `spring.mail.host` | `smtp.gmail.com` | SMTP server |
| `spring.data.web.pageable.one-indexed-parameters` | `true` | Pagination starts from page 1 |

## 7. How to Run the Project

### 7.1. Environment Requirements

- JDK 21
- MySQL
- Maven Wrapper included in the project
- A Gmail account and App Password if you want email sending to work

### 7.2. Prepare the Database

Create the database:

```sql
CREATE DATABASE jobhunter;
```

### 7.3. Review and Update Configuration

Update the following values in `application.properties` to match your environment:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.base64-secret`
- `upload-file.base-uri`
- `spring.mail.username`
- `spring.mail.password`

### 7.4. Start the Application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

After startup:

- API base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 8. Automatic Seed Data

`DatabaseInitializer` seeds initial data when the database tables are empty:

- A list of permissions for the modules: company, job, permission, resume, role, user, subscriber, and file.
- Default role: `SUPER_ADMIN`
- Default user:

```text
email: admin@gmail.com
password: 123456
```

Notes:

- Passwords are encoded with BCrypt before being stored.
- If data already exists, the seed process is skipped.

## 9. Data Model

### 9.1. Main Entities

- `User`: system user.
- `Role`: user role.
- `Permission`: endpoint access permission.
- `Company`: recruiting company.
- `Job`: job posting.
- `Skill`: skill.
- `Resume`: job application resume.
- `Subscriber`: user subscribed to receive job emails.

### 9.2. Relationships

- `Company` 1-N `User`
- `Company` 1-N `Job`
- `Role` 1-N `User`
- `Role` N-N `Permission`
- `Job` N-N `Skill`
- `Subscriber` N-N `Skill`
- `User` 1-N `Resume`
- `Job` 1-N `Resume`

### 9.3. Enums

- `GenderEnum`: `MALE`, `FEMALE`, `OTHER`
- `LevelEnum`: `INTERN`, `FRESHER`, `MIDDLE`, `SENIOR`, `JUNIOR`
- `ResumeStateEnum`: `PENDING`, `REVIEWING`, `APPROVED`, `REJECTED`

### 9.4. Automatic Audit Fields

Most entities contain:

- `createdAt`
- `updatedAt`
- `createdBy`
- `updatedBy`

These fields are populated through `@PrePersist` and `@PreUpdate`, using the current authenticated user from `SecurityUtil`.

## 10. Authentication and Authorization

### 10.1. JWT

The system uses JWT signed with the `HS512` algorithm.

- The access token is returned in the login response body.
- The refresh token is stored in the `refresh_token` cookie.
- `/api/v1/auth/refresh` uses that cookie to issue a new access token and refresh token.

### 10.2. Authentication Flow

`SecurityConfiguration` sets up:

- Stateless sessions with `SessionCreationPolicy.STATELESS`
- CSRF disabled
- Bearer token authentication
- `CustomAuthenticationEntryPoint` for JSON-formatted unauthorized responses

### 10.3. Authorization Flow

In addition to JWT authentication, the project uses `PermissionInterceptor` to validate access based on:

- The current email in the security context
- The current user from the database
- The user's role
- The permission list assigned to that role
- The request `apiPath + method`

### 10.4. Public Endpoints

The following endpoints are currently public or do not require a Bearer token:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `GET /api/v1/auth/refresh`
- `GET /api/v1/companies/**`
- `GET /api/v1/jobs/**`
- `GET /api/v1/skills/**`
- `GET /api/v1/email/**`
- `/storage/**`
- `/swagger-ui/**`
- `/v3/api-docs/**`

### 10.5. Important Note About Current Permission Checks

Due to the current `PermissionInterceptorConfiguration`, some endpoint groups do not pass through database-driven permission checks, for example:

- `companies`
- `jobs`
- `skills`
- `files`
- `resumes`
- `subscribers`
- `email`

This means:

- Some endpoints still require authentication because Spring Security enforces it.
- But once authenticated, those endpoints are not additionally blocked by the `permissions` table.

## 11. CORS

The API allows requests from these frontend origins:

- `http://localhost:3000`
- `http://localhost:4173`
- `http://localhost:5173`

Allowed methods:

- `GET`
- `POST`
- `PUT`
- `DELETE`
- `OPTIONS`

## 12. Response Format

### 12.1. Success Response

Most endpoints are wrapped by `FormatRestResponse` into this structure:

```json
{
  "statusCode": 200,
  "message": "CALL API SUCCESS",
  "data": {}
}
```

If a method uses the `@ApiMessage` annotation, the `message` field uses that value instead.

### 12.2. Error Response

Errors usually look like this:

```json
{
  "statusCode": 400,
  "error": "Exception occurs...",
  "message": "Error details"
}
```

### 12.3. Paginated Response

List APIs usually return the following structure inside `data`:

```json
{
  "meta": {
    "page": 1,
    "pageSize": 10,
    "pages": 5,
    "total": 50
  },
  "result": []
}
```

## 13. Filtering and Pagination

Many list endpoints support:

- `page`
- `size`
- `sort`
- `filter`

The project uses `spring-filter` with `Specification<T>`, which enables dynamic filtering for:

- company
- job
- permission
- resume
- role
- skill
- user

Example queries:

```text
GET /api/v1/jobs?page=1&size=10
GET /api/v1/users?page=1&size=20&sort=id,desc
```

Note:

- The current configuration uses `one-indexed-parameters=true`, so the client should send `page=1` for the first page.

## 14. API List

### 14.1. Auth

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Login | Public |
| `GET` | `/api/v1/auth/account` | Get current account information | JWT |
| `GET` | `/api/v1/auth/refresh` | Refresh token using cookie | Public |
| `POST` | `/api/v1/auth/logout` | Logout | JWT |
| `POST` | `/api/v1/auth/register` | Register a new user | Public |

### 14.2. User

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/users` | Create user | JWT + Permission |
| `GET` | `/api/v1/users/{id}` | Get user by id | JWT + Permission |
| `GET` | `/api/v1/users` | Get user list | JWT + Permission |
| `PUT` | `/api/v1/users` | Update user | JWT + Permission |
| `DELETE` | `/api/v1/users/{id}` | Delete user | JWT + Permission |

### 14.3. Company

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/companies` | Create company | JWT |
| `GET` | `/api/v1/companies/{id}` | Get company by id | Public |
| `GET` | `/api/v1/companies` | Get company list | Public |
| `PUT` | `/api/v1/companies` | Update company | JWT |
| `DELETE` | `/api/v1/companies/{id}` | Delete company | JWT |

### 14.4. Job

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/jobs` | Create job | JWT |
| `PUT` | `/api/v1/jobs` | Update job | JWT |
| `GET` | `/api/v1/jobs/{id}` | Get job by id | Public |
| `GET` | `/api/v1/jobs` | Get job list | Public |
| `DELETE` | `/api/v1/jobs/{id}` | Delete job | JWT |

### 14.5. Skill

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/skills/create` | Create skill | JWT |
| `PUT` | `/api/v1/skills/update` | Update skill | JWT |
| `GET` | `/api/v1/skills/{id}` | Get skill by id | Public |
| `GET` | `/api/v1/skills` | Get skill list | Public |
| `DELETE` | `/api/v1/skills/{id}` | Delete skill | JWT |

### 14.6. Role

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/roles` | Create role | JWT + Permission |
| `PUT` | `/api/v1/roles` | Update role | JWT + Permission |
| `GET` | `/api/v1/roles/{id}` | Get role by id | JWT + Permission |
| `GET` | `/api/v1/roles` | Get role list | JWT + Permission |
| `DELETE` | `/api/v1/roles/{id}` | Delete role | JWT + Permission |

### 14.7. Permission

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/permissions` | Create permission | JWT + Permission |
| `PUT` | `/api/v1/permissions` | Update permission | JWT + Permission |
| `GET` | `/api/v1/permissions` | Get permission list | JWT + Permission |
| `DELETE` | `/api/v1/permissions/{id}` | Delete permission | JWT + Permission |

### 14.8. Resume

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/resumes` | Create resume | JWT |
| `PUT` | `/api/v1/resumes` | Update resume status | JWT |
| `DELETE` | `/api/v1/resumes/{id}` | Delete resume | JWT |
| `GET` | `/api/v1/resumes/{id}` | Get resume by id | JWT |
| `GET` | `/api/v1/resumes` | Get resume list | JWT |
| `POST` | `/api/v1/resumes/by-user` | Get resumes for the current user | JWT |

### 14.9. Subscriber

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/subscribers` | Create subscriber | JWT |
| `PUT` | `/api/v1/subscribers` | Update subscriber | JWT |
| `POST` | `/api/v1/subscribers/skills` | Get subscriber by current email | JWT |

### 14.10. File

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/api/v1/files` | Upload file | JWT |
| `GET` | `/api/v1/files` | Download file | JWT |

### 14.11. Email

| Method | Path | Description | Auth |
|---|---|---|---|
| `GET` | `/api/v1/email` | Send job emails to subscribers | Public |

## 15. Main Request DTO Examples

### 15.1. Login

```json
{
  "userName": "admin@gmail.com",
  "password": "123456"
}
```

### 15.2. Create User

```json
{
  "name": "Nguyen Van A",
  "email": "a@gmail.com",
  "password": "123456",
  "age": 22,
  "gender": "MALE",
  "address": "HCM",
  "role": { "id": 1 },
  "company": { "id": 1 }
}
```

### 15.3. Update User

```json
{
  "id": 1,
  "name": "Nguyen Van A Updated",
  "email": "a.updated@gmail.com",
  "age": 23,
  "gender": "MALE",
  "address": "Da Nang",
  "role": { "id": 1 },
  "company": { "id": 1 }
}
```

### 15.4. Create Job

```json
{
  "name": "Java Backend Developer",
  "location": "Ho Chi Minh",
  "salary": 2000,
  "quantity": 2,
  "level": "MIDDLE",
  "active": true,
  "description": "Spring Boot, MySQL, REST API",
  "company": { "id": 1 },
  "skills": [
    { "id": 1 },
    { "id": 2 }
  ]
}
```

### 15.5. Create Resume

```json
{
  "email": "candidate@gmail.com",
  "url": "cv/candidate-cv.pdf",
  "status": "PENDING",
  "userId": 1,
  "jobId": 1
}
```

## 16. File Upload

### 16.1. Upload API

`POST /api/v1/files`

Form-data:

- `file`: file to upload
- `folder`: destination subfolder, for example `cv`, `avatar`, or `company`

### 16.2. Allowed File Types

- `pdf`
- `jpg`
- `jpeg`
- `png`
- `doc`
- `docx`

### 16.3. Limits

- Maximum file size: `50MB`
- Maximum request size: `50MB`

### 16.4. Static File Access

Uploaded files are exposed through:

```text
/storage/**
```

## 17. Email Sending

### 17.1. Mechanism

- A subscriber registers the skills they care about.
- When `GET /api/v1/email` is called, the system loads all subscribers.
- For each subscriber, it finds jobs with matching skills.
- The data is rendered through the Thymeleaf template `templates/job.html`.
- Emails are sent asynchronously using `@Async`.

### 17.2. Scheduling

The application enables:

- `@EnableAsync`
- `@EnableScheduling`

However, at the moment:

- Email sending is triggered manually through the controller endpoint.
- The `@Scheduled` annotation in `EmailController` is currently commented out.

## 18. Swagger / OpenAPI

`OpenSwaggerConfig` defines:

- API name: `Job Hunter API`
- Version: `1.0`
- Bearer token security in Swagger
- Two sample servers:
  - `http://localhost:8080`
  - `http://localhost:8081`

## 19. Notable Business Logic

- `ResumeController.fetchAll` limits resumes to jobs owned by the current user's company.
- `ResumeService.fetchResumeByUser` filters resumes by the current authenticated user's email.
- `SkillService.deleteSkill` removes many-to-many links with `jobs` and `subscribers` before deleting a skill.
- `PermissionService.deletePermission` removes `permission_role` links before deleting a permission.
- `CompanyService.deleteCompany` currently deletes all users that belong to the company before deleting the company.

## 20. Current Technical Notes

These are useful points to keep in mind when continuing development:

- Some update endpoints still accept entities directly instead of dedicated DTOs, for example company, role, permission, and subscriber.
- `application.properties` currently contains local configuration and real secrets. For other environments, these should be moved to environment variables or separate config files.
- `upload-file.base-uri` is currently a Windows absolute path, which is not suitable for Linux or container deployments.
- The refresh token cookie is set with `secure=true`, so its behavior should be verified when running locally over plain HTTP.
- The automated test suite is still very thin and currently only includes a basic context-load test.

## 21. Testing

Run tests:

```powershell
.\mvnw.cmd test
```

Build the package:

```powershell
.\mvnw.cmd clean package
```

The current test folder only contains:

- `JobRecruitmentApplicationTests`: a basic Spring context startup test.

## 22. Reasonable Next Improvements

- Move all update endpoints to dedicated request DTOs.
- Add tests for controller, service, and repository layers.
- Split configuration into `dev`, `test`, and `prod` profiles.
- Standardize input validation across all requests.
- Complete scheduled email delivery.
- Move file storage to S3 or another object storage service if the system needs to scale.
- Add a separate frontend README if the project includes a client application.

## 23. Quick Onboarding Guide

If you are new to this codebase, a good reading order is:

1. `application.properties`
2. `SecurityConfiguration`, `PermissionInterceptor`, `DatabaseInitializer`
3. `domain/*`
4. `controller/*`
5. `service/impl/*`
6. `util/SecurityUtil` and `util/FormatRestResponse`

This gives you a fast understanding of:

- the data model
- the authentication flow
- the authorization model
- the response format
- the business flow of each module
