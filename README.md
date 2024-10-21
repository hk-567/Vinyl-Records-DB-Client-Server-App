# Vinyl Records Database Client-Server Application

A Java-based client-server application for accessing a records database. This project demonstrates several key skills in Java development, particularly in GUI design, network programming, database interaction, and multithreading.

## Table of Contents

- [Key Skills and Concepts](#key-skills-and-concepts)
- [Installation](#installation)
- [Usage](#usage)
  
## Key Skills and Concepts

- **JavaFX Application Development**
  - UI Development with JavaFX components (TextField, Label, Button, TableView)
  - Event Handling and Data Binding

- **Network Programming (Sockets)**
  - Client-side and server-side networking using TCP/IP sockets
  - Data transmission using InputStream and OutputStream

- **Multi-threaded Programming**
  - Handling concurrent client requests using threads

- **Database Interaction using JDBC**
  - Connecting to a database and executing SQL queries
  - Using PreparedStatement and ResultSet

- **Object Serialization**
  - Serialization of objects for network transfer

- **SQL Query Construction**
  - Dynamic querying based on user input

- **Exception Handling**
  - Handling various exceptions related to I/O, networking, and SQL

- **Separation of Concerns and Layered Architecture**
  - Clear separation of client and server functionalities

- **Concurrency Control**
  - Managing multiple client connections simultaneously

- **Data Transformation and Display**
  - Transforming data for display in JavaFX TableView

- **Client-Server Lifecycle Management**
  - Establishing and terminating socket connections

- **Custom Data Structures**
  - Creating custom classes for data representation

## Installation

1. Clone the repository to your local machine.
2. Ensure you have Java JDK installed.
3. Compile the Java files using:
   ```bash
   javac *.java
4. Run the server:
   ```bash
   java RecordsDatabaseServer
5. In a separate terminal, run the client
   ```bash
   java RecordsDatabaseClient

## Usage
1. Enter the artist's surname and the record shop's city in the input fields.
2. Click on the "Request Records Database Service" button to fetch data.
3. The results will be displayed in the table below.


   
