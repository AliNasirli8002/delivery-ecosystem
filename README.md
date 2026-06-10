# Distributed Delivery & Payment Microservices Pipeline

A modern, high-performance, event-driven microservices ecosystem designed to handle order orchestration, courier tracking, and automated wallet payouts. Built natively using Java 26, Spring Boot 3.x, RabbitMQ, and PostgreSQL, managed within a unified Gradle multi-project monorepo.

---

## Architecture Overview

The system consists of three independent backend components that communicate using both synchronous RPC (Remote Procedure Call) protocols and asynchronous event-driven message networks:

* **Client API** sends a request directly to **ms-order** (Port 8081).
* **ms-order** (Port 8081) performs a synchronous REST handshake using **Spring Cloud OpenFeign** with **ms-courier** (Port 8082) to secure an available driver, persists the transaction, and broadcasts an event to **RabbitMQ**.
* **ms-courier** (Port 8082) manages courier rosters, operational availability states, and profile attributes.
* **ms-payment** (Port 8083) asynchronously consumes the order creation payloads from **RabbitMQ**, logs customer transactions, maps entity relations, and directly calculates and updates courier balance ledgers.

---

## Tech Stack & Ecosystem

* **Runtime & Framework**: Java 26, Spring Boot 3.x
* **Build System**: Gradle (Multi-Project Build Layout)
* **Inter-Service Communication**: 
  * Synchronous: Spring Cloud OpenFeign
  * Asynchronous: RabbitMQ (AMQP Broker)
* **Database & Migration Engine**: PostgreSQL with Liquibase database evolution scripting
* **Containerization**: Docker & Docker Compose

---

## Repository Structure

* **delivery-ecosystem/** (Common Monorepo Folder)
  * **README.md** (Project Documentation Homepage)
  * **.gitignore** (Global Workspace Ignore Rules)
  * **docker-compose.yaml** (Infrastructure Provisioning Engine)
  * **settings.gradle** (Multi-Project Module Registration)
  * **build.gradle** (Root Gradle Script)
  * **init-scripts/** (Database Automatic Seeding Engine)
    * **init.sql**
  * **ms-order/** (Order Microservice Gradle Sub-Project)
  * **ms-courier/** (Courier Microservice Gradle Sub-Project)
  * **ms-payment/** (Payment Microservice Gradle Sub-Project)

---

## How to Run the Ecosystem

### 1. Pre-requisites
* Docker Desktop installed and running
* Java 26 SDK configured locally

### 2. Launch Infrastructure Services
Spin up your centralized Message Broker and unified Database instance. Open your terminal in the root directory (delivery-ecosystem/) and execute:

docker-compose up -d

Note: The script automatically boots PostgreSQL on port 5433 and maps the distinct underlying schemas (order_db, courier_db, and payment_db) via the entrypoint init script.

### 3. Compile and Run the Applications
Because this is configured as a Gradle multi-project workspace, you can compile all microservices simultaneously from the root directory:

./gradlew build -x test

Next, boot up each application in your IDE or use your terminal inside their respective sub-directories:
* CourierApplication (Port 8082)
* OrderApplication (Port 8081)
* PaymentApplication (Port 8083)

---

## End-to-End API Integration Walkthrough

To verify the synchronous and asynchronous data pipelines, execute the following request sequence using an API client (like Insomnia):

### Step 1: Initialize an Available Courier
POST http://localhost:8082/api/v1/couriers
Content-Type: application/json

{
  "name": "Ali Nasirli",
  "status": "AVAILABLE"
}

### Step 2: Create a Flight Order
POST http://localhost:8081/api/v1/orders
Content-Type: application/json

{
  "description": "Premium Delivery Flight Package"
}

Behind the scenes: ms-order assigns the courier via Feign, persists the transaction, and broadcasts an event message. ms-payment consumes it, initiates a balance sheet, and processes a driver payout cut.

### Step 3: Fetch the Verified Result
GET http://localhost:8081/api/v1/orders/1

Expected Consolidated JSON Response Output:
{
  "id": 1,
  "description": "Premium Delivery Flight Package",
  "courierId": 1,
  "courierName": "Ali Nasirli",
  "price": 10.00,
  "status": "ASSIGNED",
  "paymentStatus": "SUCCESS",
  "paymentProcessedAt": "2026-06-10T21:45:00"
}