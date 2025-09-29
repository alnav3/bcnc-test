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

The `PRICES` table contains:
- Product pricing information with date ranges
- Brand associations (1 = ZARA)
- Priority-based price selection
- Sample data for testing scenarios

## Development

This project uses Liquibase for database migrations located in `src/main/resources/db/changelog/`. All schema changes should be versioned through migration files.

## WIP

This README file is a work in progress up until the finalization of the test. It will be changing and adding context.
