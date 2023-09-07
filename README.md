# EDTS Back-end Engineer Technical Assessment

Written by Daniel Loewito.\
API Documentation is available [here](https://documenter.getpostman.com/view/20788941/2s9YBz3aay "https://documenter.getpostman.com/view/20788941/2s9YBz3aay").
Database Design Document is available [here](EDTSDDD.pdf)

## Set-Up

 1. Set up a PostgreSQL database and run [DBCreateScript.sql](DBCreateScript.sql) to create
    the database designed for this back-end.
 2. Alter /src/main/resources/application.properties to reflect your PostgreSQL database information:\
    `spring.datasource.url=jdbc:postgresql://localhost:5432/EDTSEmployees`\
    `spring.datasource.username=my_user`\
    `spring.datasource.password=my_password`
    
 3.  Run with `mvn spring-boot:run`


By default, this back-end will listen on port 8080. The API Documentation reflects this.
