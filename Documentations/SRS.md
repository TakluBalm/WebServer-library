# Software Requirements Specification (SRS)

## 1. Introduction

### 1.1 Purpose of the Project

The purpose of this project is to gain a comprehensive understanding of two widely used frameworks, Flask and Spring Boot, and their respective design and working principles. By studying and implementing these frameworks, we aim to enhance our knowledge and expertise in developing web applications. Flask, a Python-based microframework, offers a lightweight and flexible approach for building web applications. On the other hand, Spring Boot, a Java-based framework, provides a powerful and opinionated framework for developing enterprise-grade applications. Through this project, we will explore the core concepts, architectural patterns, and features of these frameworks, enabling us to leverage their capabilities and apply them effectively in our own software development projects.

### 1.2 Scope of the Project

The scope of this project is to develop a lightweight and user-friendly web application framework. Our goal is to provide a high-performance web server or application server, optimizing the performance and scalability of web applications. The framework will be flexible and extensible, allowing customization to meet diverse application needs. Overall, our project focuses on delivering a lightweight, easy-to-use framework that enhances web application development, ensuring performance, scalability, flexibility, best practices, and community-driven evolution.

## 2. Overall Description of the Project

The project aims to design a server-based web application framework. Our focus is on developing a robust framework that enables the creation of web applications with ease. It will provide the necessary tools and components for server-side development. The framework will simplify the process of building web applications and empower developers to create server-based solutions efficiently. We have also made our own ORM which is compatible with postgres. With the ORM package, Users can quickly create class which map to database in postgres. User can delete ,insert and update objects. Users can create queries. We have also brainstormed a jinja-esque templating language which enables your HTML to be more dynamic. Our HTTP request/response generating classes provide easy access to the underlying HTTP project.



## 3. Software Stack

Our software stack consists of a set of technologies and tools that work together to support the development of our project. We are utilizing Java as the primary programming language, which offers a wide range of libraries and frameworks for building robust applications.

For database connectivity, we have chosen Postgres JDBC, a Java Database Connectivity driver specifically designed for PostgreSQL databases. This driver allows seamless interaction with the database, enabling efficient data retrieval and manipulation.

To manage our project dependencies and build automation, we are employing Gradle, a powerful build tool. Gradle simplifies the process of managing dependencies, compiling source code, running tests, and generating executable artifacts. It offers flexibility and scalability, making it an excellent choice for our project.

In terms of libraries, we are leveraging several key ones. Reflection is a library that enables us to inspect and manipulate the structure and behavior of classes at runtime. This capability allows dynamic access to class members, facilitating tasks such as object creation, method invocation, and field manipulation.

ClassGraph is another library we are using, which provides fast, efficient, and comprehensive classpath scanning capabilities. It allows us to scan the classpath to discover classes, packages, methods, and fields, making it useful for tasks like automatic configuration, plugin discovery, and annotation processing.

For HTML parsing and manipulation, we have incorporated Jsoup, a Java library that provides a convenient API for working with HTML documents. Jsoup simplifies tasks such as parsing HTML, extracting data, manipulating elements, and generating HTML. It enables efficient web scraping, data extraction, and content manipulation within our application.

By utilizing this software stack, we are leveraging the capabilities and features of these technologies to build a robust and efficient web application. The combination of Java, Postgres JDBC, Gradle, Reflection, ClassGraph, and Jsoup enables us to develop a scalable, performant, and feature-rich solution.

## 4. Flow Diagram 
<img src="./flowDiagram.jpeg" alt="Getting started" />


## 5. Contributors
- Jugal Chapatwala (2020csb1082)
- Prakhar Saxena (2020csb1111)
- Tanuj Kumar (2020csb1134)
- Gautam Sethia (2020csb1086)

##  Thanks!