
Driver-Test
===========

ABDN project.
=============

My Quiz App Simple Quiz application.

About app:

*  app uses SQL Database to store question data. Database is created with DB Browser and added into project assed directory. 
   From asset dir in compile time method check if database exist in android device in DB_PATH "/data/data/project_name/databases/".
   the method checks if a database with DB_NAME is created in the directory and if it does not exist call other method to create it.
   In this way database can be created outside project and later added to app.

* AddQuestion Activity - in this layout_activity user add new rows /questions/ in database table;

*    Result Activity - this layout_activity display user scores and have check box for showing all question data;

*    User have button to share Quiz result via email.

![alt text](https://i.ibb.co/wSvJ2gv/Driver-Test-App.jpg)

