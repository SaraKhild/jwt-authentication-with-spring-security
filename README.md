# JWT Authentication with Spring Security.
![jwtwithspringsecurtiy](https://github.com/user-attachments/assets/7ba29e75-211e-495b-8d16-f7af5d4fad34)

<br>

## Overview
In this tutorial, we will build a user authentication service using  <strong>Spring Security, JWT (JSON Web Tokens), and Mysql.</strong> The application will allow users to <mark>register, log in, and access a protected user profile API</mark>, demonstrating how to implement <mark>user authentication and authorisation using JWT.</mark>
  
## Usages
- Spring Security
- JWT
- MySQL

## Architecture of each the Projects

 ### 1-src folder
   - configration
       - filter
       - security
   - controller
   - dto
   - exception
   - mapper
     - impl
   - model
   - repository
   - service
     - impl
     
 ### 2-resources folder
   - application.properties
   - user.sql
   
### 3-Maven pom.xml
<br> 
    
```
	<dependencies>
	 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>3.0.2</version>
        </dependency>
        <!-- json web token -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.6</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
	</dependencies>
 ```
<br>

###### Output :star_struck: 

##### :pencil2: This is `a publicly accessible API endpoint` that returns a simple response without requiring `a JWT token` or any `authentication`. It can be accessed by anyone.

<img width="1313" alt="public-resource" src="https://github.com/user-attachments/assets/1e2a1ed0-f2fd-43dc-a29d-a457568e7aa2"/>

##### :pencil2: This video demonstrates the full authentication flow using `Spring Security` and `JWT`. It begins with a user signing up for a new account, followed by a login request to obtain a JWT token. The token is then used to access a `protected user profile endpoint`, showcasing stateless authentication.

https://github.com/user-attachments/assets/91db9d20-4c42-47e1-88e5-0e3a94a93770

##### :pencil2: This video shows what happens when a client attempts to access `a protected API` without providing a valid JWT token. It highlights how the backend properly returns `an unauthorized (401)` response, demonstra

https://github.com/user-attachments/assets/b7540750-f46b-446a-bd36-148e1916fe02


ting JWT-based access control in action.


---

### Good Luck <img src="https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif" width="30px"> 
