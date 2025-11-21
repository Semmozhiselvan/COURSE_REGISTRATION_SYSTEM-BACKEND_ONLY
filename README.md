
# Course Registration System (Spring Boot + Spring Security - JWT)

Frontend code link - https://github.com/Semmozhiselvan/COURSE-REGISTRATION-SYSTEM-FRONTEND_ONLY.git

Features:
- User registration (stored in H2 DB)
- BCrypt password encoding
- JWT authentication
- Role-based authorization (ROLE_ADMIN, ROLE_STUDENT)
- APIs:
  - POST /auth/register  {email, password, role}
  - POST /auth/login     {email, password} -> returns token
  - GET  /courses/all
  - POST /courses/add    (ADMIN)
  - POST /courses/enroll (STUDENT) ?courseId=<id>  (Requires Bearer token)
  - GET  /courses/enrolled (STUDENT)
  - GET  /courses/enrolled/students?courseId=<id>  (ADMIN)

H2 Console: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:coursedb)

Instructions:
1. Build: mvn clean package
2. Run: java -jar target/course-registration-0.0.1-SNAPSHOT.jar
3. Register users via /auth/register. Use role value 'ROLE_ADMIN' or 'ROLE_STUDENT'.
4. Login via /auth/login to receive JWT. Add `Authorization: Bearer <token>` header for protected endpoints.
