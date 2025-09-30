# E-commerce Pricing Service

Working on a pricing service for an e-commerce platform. The idea is to query product prices based on date, product, and brand.

## Architecture

The application follows hexagonal architecture principles with clear separation between domain, application, and infrastructure layers.

## Database

The application uses H2 in-memory database with Liquibase for schema management.

**H2 Console:** `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## API Endpoints

- `GET /api/health` - Health check endpoint

## Database Schema

The database now includes three main entities with proper relationships:

### BRAND Table
- Brand information with name, description, and creation timestamp
- One-to-many relationship with prices

### PRODUCT Table  
- Product catalog with unique codes, names, descriptions, categories
- One-to-many relationship with prices

### PRICES Table
- Product pricing information with date ranges
- Foreign key relationships to both BRAND and PRODUCT tables
- Priority-based price selection
- Sample data for testing scenarios

All tables are properly normalized with foreign key constraints and include audit fields for tracking creation timestamps.

## JPA Entities

The infrastructure layer includes JPA entities with Lombok annotations for reduced boilerplate:
- `BrandEntity` - Brand management with lazy-loaded price relationships
- `ProductEntity` - Product catalog with unique code constraints  
- `PriceEntity` - Pricing data with proper foreign key mappings

## Repository Layer

Spring Data JPA repositories are implemented for each entity:
- `JpaBrandRepository` - Includes custom finder by brand name
- `JpaProductRepository` - Basic CRUD operations
- `JpaPriceRepository` - Basic CRUD operations

## Development

This project uses Liquibase for database migrations located in `src/main/resources/db/changelog/`. All schema changes should be versioned through migration files. The current migration sequence creates the pricing table first, then adds the normalized brand and product tables with foreign key relationships.

## WIP

This README file is a work in progress up until the finalization of the test. It will be changing and adding context.
