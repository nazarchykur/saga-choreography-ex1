# Saga-Choreography

Saga Choreography is a sample project showcasing a distributed microservices architecture implemented using Spring Cloud, Kafka, and PostgreSQL. This repository provides an example of how to implement the choreography-based Saga pattern for orchestrating a complex business process involving multiple services.

![img_2.png](screenshots%2Fimg_2.png)

## When to Use Saga-Choreography

Saga Choreography is suitable for scenarios where you need to coordinate multiple microservices to complete a business process that involves multiple steps, services, and potentially distributed transactions. This pattern is helpful in scenarios like order processing, where various actions must be taken in response to certain events.

![img.png](screenshots%2Fimg.png)

## Project Structure

This repository consists of three microservices: `order-service`, `payment-service`, and `inventory-service`, along with a `common` module shared between them. Below, we provide an overview of the project structure and key components of each module:

### Common

- **DTOs**: Contains data transfer objects shared between microservices, including `InventoryDto`, `OrderRequestDto`, `OrderResponseDto`, and `PaymentDto`.

- **Events**: Defines event classes used for communication between microservices, such as `InventoryEvent`, `OrderEvent`, and `PaymentEvent`.

### Inventory-Service

- **Configuration**: Defines the `InventoryConfig` class responsible for configuring the Inventory service, including defining the `inventoryProcessor` function to process events.

- **Server Configuration**: Contains configuration settings for the Inventory service, such as port, application name, database configuration, and H2 console settings.

### Order-Service

- **Configuration**: Defines the `EventHandlersConfig` class responsible for configuring event handlers in the Order service, including `paymentEventConsumer` and `inventoryEventConsumer`.

- **Order Configuration**: Contains the `OrderConfig` class, defining the order sink and supplier.

- **Order Status Update**: Implements the `OrderStatusUpdateEventHandler` class to update the order status based on payment and inventory events.

- **Server Configuration**: Contains configuration settings for the Order service, including port, application name, database configuration, and H2 console settings.

### Payment-Service

- **Configuration**: Defines the `PaymentConfig` class responsible for configuring the Payment service, including defining the `paymentProcessor` function to process events.

- **Server Configuration**: Contains configuration settings for the Payment service, including port, application name, database configuration, and H2 console settings.

## Docker

- `docker-compose/postgres.yml`: Docker Compose configuration for PostgreSQL.

- `docker-compose/kafka.yml`: Docker Compose configuration for Kafka.
- 
![img_17.png](screenshots%2Fimg_17.png)

![img_18.png](screenshots%2Fimg_18.png)

![img_14.png](screenshots%2Fimg_14.png)

![img_6.png](screenshots%2Fimg_6.png)

## Usage

1. Clone this repository to your local environment.
2. Run the required Docker containers using `docker compose ... up`.
3. Build and run each microservice (`order-service`, `payment-service`, and `inventory-service`) using Spring Boot.

## Dependencies

This project relies on the following technologies and dependencies:

- Spring Boot and Spring Cloud for microservice development.
- Kafka for event-driven communication.
- PostgreSQL for data storage.
- Lombok for reducing boilerplate code.
- H2 for database console access.
- Spring Cloud Stream and Spring Kafka for stream processing.
- Project Reactor for reactive programming.


## Cloud Properties

The Saga Choreography project leverages Spring Cloud Stream for managing event-driven communication between microservices. Here are the key cloud properties used in the project:

### Function Definitions

In Spring Cloud Stream, function definitions determine how messages are processed within a microservice. In this project, the following function definitions are used:

- **orderSupplier**: This function acts as a supplier of order events. It is responsible for emitting order-related events to be consumed by other microservices.

- **paymentEventConsumer**: This function is a consumer of payment events. It processes payment-related events and updates the order status accordingly.

- **inventoryEventConsumer**: This function consumes inventory events and updates the order status based on inventory-related events.

### Stream Bindings

Stream bindings define the input and output channels for message processing. In Saga Choreography, the following stream bindings are configured:

- **orderSupplier-out-0**: This binding connects the `orderSupplier` function to the `order-event` destination. It sends order events to the `order-event` channel.

- **paymentEventConsumer-in-0**: This binding connects the `paymentEventConsumer` function to the `payment-event` destination. It listens for payment events on the `payment-event` channel.

- **inventoryEventConsumer-in-0**: This binding connects the `inventoryEventConsumer` function to the `inventory-event` destination. It listens for inventory events on the `inventory-event` channel.

These stream bindings orchestrate the flow of events between microservices, ensuring that they react to specific events and maintain the consistency of the distributed transaction.

By configuring these cloud properties, Saga Choreography implements a choreography-based saga pattern that enables seamless coordination and communication between microservices.

### !!! Please refer to the specific microservice directories for more detailed READMEs and usage instructions for each service.


This section provides a clearer explanation of how Spring Cloud Stream and the defined function and stream bindings are used to handle event-driven communication within the project.