# JavaConsoleApp
Simple console application using JDBC with SQLite.

Application provides a basic functionality for managing tasks, users and projects (creation, deletion).

Each task has following attributes:  
 - Project
 - Type
 - Priority
 - Topic
 - User
 - Description

Tasks can be filtered by project and by user.


It is not possible to delete a user or a project connected to a task.

Each task must have a unique topic.

Each user and project must have a unique name.


![startView](https://github.com/ViktorAnchutin/JavaConsoleApp/blob/master/img/FirstView.PNG?raw=true)

All data is stored in SQLite database . User can select it's own .sqlite file (it must match the database schema). 


![tables](https://github.com/ViktorAnchutin/JavaConsoleApp/blob/master/img/tables.PNG?raw=true)

To run the application type :
```
java -jar JavaConsoleApp.jar
```
in project directory.

There are two sqlite files with set schemas. By default file "db.sqlite" is selected. To change database file after you run the app type:
```
change db
```
