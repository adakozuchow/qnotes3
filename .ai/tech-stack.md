# Technical Specification for Qnotes2

## Programming Languages

- BE (BackEnd) is written in Java 17
- FE (FrontEnd) is written in TypeScript

## Data Storage
- MongoDB

## Frameworks
### FE Solutions
- Angular 20
### BE Solutions
- Spring 3.* family including: Spring Boot, Spring Data MongoDB, Spring Security
- Junit 5 for unit testing
- Flapdoodle solution for running local MongoDB 

## Communication between BE and FE
- Communication betweween BE and FE is done in REST style
- Endpoint definitions are defined in separate yaml document in OpenAPI 3.1 format specification
