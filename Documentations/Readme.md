# WEB-FRAMEWORK
The project aims to design a server-based web application framework. Our focus is on developing a robust framework that enables the creation of web applications with ease. It will provide the necessary tools and components for server-side development. The framework will simplify the process of building web applications and empower developers to create server-based solutions efficiently. We have also made our own ORM which is compatible with postgres. With the ORM package, Users can quickly create class which map to database in postgres. User can delete ,insert and update objects. Users can create queries.

## REQUIREMENTS
- JDK version 11.0.18

   If JDK is not already installed on the machine , please refer to the following link:
   - [JDK installation guide](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)

- Gradle version 8.0
  If gradle is not already installed on the machine , please refer to the following link:
   - [Gradle Release](https://gradle.org/releases/) 

- Postgres 
  If postgres is not already installed on the machine , please refer to the following link:
   - [Postgresql download](https://www.postgresql.org/download//)
   - [pgJDBC download](https://jdbc.postgresql.org/) 

## INSTALLATION
1. Download and extract the zip folder of the project .
2. Open a terminal and navigate to the root directory (*******) using the `cd` command 
3. Open the configFile.txt from  src/test/java/ORM/Manager , add your URL in first line ,User in second line ,Password in third line .
For example :
      ```
      URL=jdbc:postgresql://localhost:{port}/{db_name}
      User=XYZ
      Password=XYZ
      ```
   where XYZ are respective details wrt to your postgres.

4. Run command
   ```
   ./gradlew clean build test
   ```
   to generate a new build of the project . This will also run the unit tests of the application . Gradle will automatically add all the required dependencies for the project .
  
## API DOCUMENTATION
1. To see the ORM API's documentation navigate to the
   - [ORM API](./ORM/Documentation/index.html)


2. To see the Server API's documentation navigate to the 
   - [Server API](./Server/index.html)
   


##  Thanks!



