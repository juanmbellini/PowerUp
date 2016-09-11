#Database scripts
These scripts generate and populate the database with basic data extracted from [IGDB] http://www.igdb.com.

##Setting up
1. Make sure you have PostgreSQL **>= 9.5** installed first. [Download here] https://www.postgresql.org/download/.
2. Add Postgres' bin directory (something like ```C:\Program Files\PostgreSQL\9.6\bin```) to your PATH environment variable so you can run ```psql``` from the command line. You may have to restart for this to take effect.
3. Open a terminal in this directory (where the .sql files are)
4. Make sure the postgreSQL server is running (in Windows, it's set to auto start after installation)
5. Log into Postgres with ```psql -U postgres``` and the password you set during the install.

##(Re-)creating the Tables
1. Make sure you're logged into Postgres (see Setting Up).
2. Run ```\i pawDB.sql```
3. You should get a bunch of ```DROP TABLE```s, ```CREATE TABLE```s and a final ```COMMIT```.

##Populating the tables
1. Make sure the tables have been created (see Creating the Tables).
2. Run ```\i populate.sql```
3. You should get a bunch of ```INSERT```s and a final ```COMMIT```.