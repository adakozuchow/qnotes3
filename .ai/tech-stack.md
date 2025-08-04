# Technical Specification for Qnotes3

## Programming Languages

- BE (BackEnd) is written in Java 21
- FE (FrontEnd) is written in TypeScript

## Data Storage
- MongoDB

## Frameworks
### FE Solutions
- Angular 20
### BE Solutions
- Spring Boot 3, Spring 6 including support for Spring Web, Spring Data MongoDB and Spring Security
- Junit 5 for unit testing
- Flapdoodle solution for running local MongoDB 

## Communication between BE and FE
- Communication betweween BE and FE is done in REST style
- Endpoint definitions are defined in separate yaml document in OpenAPI 3.1 format specification
