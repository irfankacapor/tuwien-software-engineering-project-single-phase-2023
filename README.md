# Software Engineering Project Single Phase - Technical University of Vienna 2023

This repository contains an application created as first part of a course at the Technical University of Vienna - "Software Engineering Project". It is called the "Single phase", and a successful implementation of the application was required to proceed to the group phase.

Since I have completed both phases and successfully passed the course, you can view the "Group phase" project, a way more complex project, here: [Software Engineering Project Group Phase - Technical University of Vienna 2023](https://github.com/irfankacapor/tuwien-software-engineering-project-group-phase-2023).

In short, this CRUD application is meant to enable tournament management for horse races. It implements the **"Layered Architecture"** consisting of four layers **persistence, service and REST in the backend** and **UI layer in the frontend**.

However, this first stage of the course required us to learn about **version control with git** and **automated testing with JUnit 5**, so we were required to make regular commits with meaningful summaries and write our own automated tests to test the implemented functionalities.

## Running the application

To run, see and test the created application on your own, the best way would be to set it up on your own machine and run it, following these steps:

1. Clone the project to a local folder
2. Navigate to the folder and inside ./backend run the following commands:
   1. `mvn clean package` to compile the backend
   2. `java -Dspring.profiles.active=datagen -jar target/e12124537-0.0.1-SNAPSHOT.jar` to insert the initial test data into the database
   3. `mvn spring-boot:run` to run the server
3. Navigate back to the root folder and then to ./frontend and run:
   1. `npm install` to install the required dependencies
   2. `ng serve` to run the frontend
4. Open your preferred browser and go to: [http://localhost:4200/](http://localhost:4200/) and play around with the application!

## Knowledge required for the implementation

To solve the problem that we were given, the project required some prior knowledge, but also the eagerness and ability to learn new skills.

Some of the technical and methodological skills that were necessary:

- Object-oriented analysis, design and programming
- Basics of the Unified Modeling Language (UML)
- Knowledge of algorithms and data structures
- Knowledge of database systems

On the other side, cognitive and practical skills were also required:
- Use a practical programming language and tools (e.g. Java) 
- Use an IDE and source code management

## Developed skills

After completing this project, I feel more confident in using **Git** for version control, as well as more able to write **meaningful commit messages** and connect them to corresponding tech and user stories. I have also learned to **keep track of the time** invested in the project, which has enabled me better organization and assessment of this and upcoming projects.

I have learned to use **JUnit 5** to write automated tests for the backend, including both **unit and integration tests**, as well as use **MockMvc** for these integration tests.

To implement the recursive display and tracking of tournament trees, I had to take a step back and **think and plan out solutions before implementation**, but also think about and then create **efficient and effective solutions to these problems**.



