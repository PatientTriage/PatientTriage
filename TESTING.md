# Testing Your Spring Boot Application

## Method 1: Using Docker Compose (Recommended)

### Start the application:
```bash
docker-compose up --build
```

This will:
- Start PostgreSQL database
- Build and start the Spring Boot backend
- Expose the app on port 8080

### Check if it's running:

1. **Check container status:**
   ```bash
   docker-compose ps
   ```

2. **View logs:**
   ```bash
   docker-compose logs backend
   ```
   Look for: `Started BackendApplication` message

3. **Test basic connectivity:**
   ```bash
   curl http://localhost:8080
   ```
   Or open in browser: http://localhost:8080

   **Note:** Since Spring Security is enabled, you'll get a 401 Unauthorized or redirect to login page. This is **normal** - it means your app is running!

4. **Check health endpoint (if actuator is enabled):**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

## Method 2: Run Locally (Without Docker)

### Prerequisites:
- Java 21 installed
- PostgreSQL running locally on port 5432
- Database `patienttriage` created

### Steps:

1. **Start PostgreSQL** (if not using Docker):
   ```bash
   # Or use your local PostgreSQL
   docker-compose up postgres -d
   ```

2. **Update application.properties** to use local database:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/patienttriage
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

3. **Run the application:**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

4. **Test:**
   ```bash
   curl http://localhost:8080
   ```

## Quick Test Commands

```bash
# Check if port 8080 is listening
lsof -i :8080

# Or on Linux
netstat -tuln | grep 8080

# Test with curl
curl -v http://localhost:8080

# Check Docker containers
docker ps

# View backend logs
docker logs triage-backend
```

## Expected Behavior

Since Spring Security is enabled and no custom endpoints exist yet:
- ✅ **200/401 response** = App is running correctly
- ✅ **Connection refused** = App is not running
- ✅ **404** = App is running but endpoint doesn't exist

## Next Steps

To test actual endpoints, you'll need to either:
1. Add REST endpoints to `UserController`
2. Temporarily disable security for testing
3. Configure security to allow certain endpoints

