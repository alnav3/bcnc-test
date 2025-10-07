# E-commerce Pricing Service

Working on a pricing service for an e-commerce platform. The idea is to query product prices based on date, product, and brand.

## Table of Contents

- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Testing](#testing)
- [Docker Containerization](#docker-containerization)
- [Database Migrations](#database-migrations)

## Architecture

The application follows hexagonal architecture principles with clear separation between domain, application, and infrastructure layers.

### Layer Structure

**Domain Layer** (domain/)
- Pure business logic with no external dependencies
- model/ - Domain entities (Price) using only Lombok for immutability
- port/ - Interfaces for repositories (PriceRepository)

**Application Layer** (application/)
- Use cases and application services
- service/PriceQueryService - Orchestrates the price lookup business logic
- service/PriceNotFoundException - Custom exception for handling missing prices
- Framework-agnostic layer that depends only on domain interfaces

**Infrastructure Layer** (infrastructure/)
- framework-specific implementations
- entity/ - JPA entities with database annotations (PriceEntity, BrandEntity, ProductEntity)
- repository/ - Spring Data JPA repositories
- adapter/PriceRepositoryAdapter - Implements domain port to bridge JPA repositories with domain layer
- mapper/PriceEntityMapper - MapStruct mapper to convert between JPA entities and domain models

**Web Layer** (web/)
- REST API
- controller/ - REST controllers (PriceController, HealthController)
- dto/PriceResponse - Data objects for API responses
- mapper/PriceResponseMapper - MapStruct mapper from domain to DTOs
- Global exception handling via ControllerExceptionHandler

## API Endpoints

### Health Check
- GET /api/health - Returns application health status

**Example:**
```bash
curl http://localhost:8080/api/health
```

**Response:**
```json
{"status":"UP"}
```

### Price Lookup
- GET /api/prices - Retrieves the applicable price for a product and brand at a specific date

**Query Parameters:**
- date (required) - Date and time in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss)
- productId (required) - Product identifier (must be positive)
- brandId (required) - Brand identifier (must be positive)

**Example:**
```bash
curl "http://localhost:8080/api/prices?date=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

**Response Fields:**
- `productId` - Product identifier
- `brandId` - Brand/chain identifier  
- `priceList` - Price list/rate to apply
- `startDate` - Application start date
- `endDate` - Application end date
- `price` - Final price to apply
- `currency` - Currency code (additional field for production readiness, as prices without currency are incomplete in real-world scenarios)

**Response Codes:**
- 200 OK - Successfully retrieved price
- 400 Bad Request - Invalid request parameters (missing, malformed, or negative values)
- 404 Not Found - No applicable price found for the query

### OpenAPI/Swagger Documentation

The API is fully documented using Swagger.

**Swagger UI:** http://localhost:8080/swagger-ui.html
**OpenAPI JSON:** http://localhost:8080/api-docs

The documentation includes:
- Detailed operation descriptions
- Parameter documentation with examples
- Request/response schemas
- Response code explanations

## Database

The application uses H2 in-memory database with Liquibase for schema management.

**H2 Console:** http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (empty)

### Database Schema

The database includes three main entities with proper relationships:

**BRAND Table**
- Brand information with name, description, and creation timestamp
- One-to-many relationship with prices

**PRODUCT Table**
- Product catalog with unique codes, names, descriptions, categories
- One-to-many relationship with prices

**PRICES Table**
- Product pricing information with date ranges
- Foreign key relationships to both BRAND and PRODUCT tables
- Priority-based price selection (higher priority = higher precedence)
- Sample data for testing scenarios

All tables are properly normalized with foreign key constraints and include audit fields for tracking creation timestamps.

### JPA Entities

The infrastructure layer includes JPA entities with Lombok annotations for reduced boilerplate:
- BrandEntity - Brand management with lazy-loaded price relationships
- ProductEntity - Product catalog with unique code constraints
- PriceEntity - Pricing data with proper foreign key mappings and dual column approach (FK relationship + plain ID column)

### Repository Layer

Spring Data JPA repositories are implemented for each entity:
- JpaBrandRepository - Basic CRUD operations
- JpaProductRepository - Basic CRUD operations
- JpaPriceRepository - Custom query for optimized price lookups with LIMIT 1

### Design Decisions

**Closed Range Date Logic (<= and >=)**
- A price is valid if startDate <= queryDate <= endDate

**Priority Ordering with LIMIT 1**
- Orders results by priority descending and fetches only the top record
- Optimized query that returns a single result directly from the database
- Ensures maximum efficiency and scalability

**Input Validation**
- Product and brand IDs must be positive (greater than 0)
- Validation occurs at the controller level with proper error responses
- Constraint violations return 400 Bad Request with detailed messages

## Testing

Apart from the mandatory tests required to complete this test, I've added additional validations to check edge-cases and overall api logic:

**Error Handling Tests:**
- Invalid product ID (404 Not Found)
- Invalid brand ID (404 Not Found)
- Date outside all price ranges (404 Not Found)
- Missing required parameters (400 Bad Request)
- Invalid date format (400 Bad Request)
- Negative or zero IDs (400 Bad Request with validation messages)

**PriceRepositoryTest** - Database layer verification
- Verifies Liquibase migrations run successfully
- Confirms sample data is loaded correctly
- Tests repository layer independently

**Test Coverage:** 46 tests covering all layers (domain, application, infrastructure, web)

## Docker Containerization

The application includes a multi-stage Dockerfile optimized for small image size and security. It can be build using the following commands:

```bash
docker build -t bcnc-app:latest .
```

Or using Podman:
```bash
podman build -t bcnc-app:latest .
```

## Database Migrations

This project uses Liquibase for database migrations located in src/main/resources/db/changelog/.

All schema changes should be versioned through migration files.

**Why Liquibase?**

This project follows the **infrastructure-as-code** principle - everything required to deploy the application from scratch should be reproducible and version-controlled. Liquibase migrations ensure the database schema evolves alongside the codebase, just like the Dockerfile ensures the runtime environment is reproducible.
