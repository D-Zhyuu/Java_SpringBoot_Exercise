# Java_SpringBoot_Exercise

As a college student who wants to "lie flat" but has to study hard due to practical reasons, I will start from this project and gradually record my learning process.  
This repository is used to practice Java SpringBoot-related technologies.

## Development Log

- 2026.3.4  
After several hours of troubleshooting, the environment configuration issue was finally resolved.  
The main problem lies in the "test/java/com/cy/store/StoreApplicationTests.java" file of the project.  
When the following code was added to the file:

```java
@Autowired
private DataSource dataSource;
```

It always prompts "Could not autowire. No beans of 'DataSource type found'". After trying various methods with GPT continuously, I accidentally discovered that if this statement

```java
private java.sql.SQLException.DataSource dataSource;
```

was used instead of the one mentioned earlier, there would be no error.  
Although the exact reasons are not yet clear, the problem has finally been resolved and the front-end and back-end services can now operate normally.

- 2026.03.07

Recently, I have been gradually working on the logic function for user registration. The greatest gain was learning how to connect the MySQL database with the Java backend using MyBatis.  
By the way, in order to manage the database more conveniently, I installed Navicat. This software was something I had heard about during my sophomore year of studying database. Unfortunately, I didn't actually use it at that time.  
What I really want to say is that many things can only be truly understood when you do them yourself.
