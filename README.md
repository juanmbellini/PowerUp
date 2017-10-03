# PowerUp! [![Build Status](https://travis-ci.org/juanmbellini/PowerUp.svg?branch=master)](https://travis-ci.org/juanmbellini/PowerUp)

Web Applications Course Project

## Getting Started

These instructions will install the development environment into your local machine

### Prerequisites

1. Clone the repository or download source code:

	```
	$ git clone https://github.com/juanmbellini/PowerUp.git
	```
	or

	```
	$ wget https://github.com/juanmbellini/PowerUp/archive/master.zip
	```
2. Install Ruby (used to build the front end)
	#### Mac OS X
	```
	$ brew install ruby
	```

	#### Ubuntu
	```
	$ sudo apt-get install ruby
	```
3. Install Sass gem

	```
	$ gem install sass
	```
4. Install Compass gem

	```
	$ gem install compass
	```
5. Install Maven
	#### Mac OS X
	```
	$ brew install maven
	```

	#### Ubuntu
	```
	$ sudo apt-get install maven
	```

	#### Other OSes
	Check https://maven.apache.org/install.html.
	
6. Create a ```passwords.properties``` file under ```<PROJECT-ROOT>/paw-webapp/webapp/src/main/resources/config``` with the following information:

	``` properties
	jwt.secret=<jwt-hash-key>
	email.password=<email-password>
	db.password=<database-password>
	```	


7. (Optional) Install PostgreSQL

	#### Mac OS X
	```
	$ brew install postgresql
	```

	#### Ubuntu
	```
	$ sudo apt-get install postgresql
	```
8. (Optional) Create a Postgres User

	```
	$ createuser -d -W <database-username>
	```	
9. (Optional) Create a Postgres Database

	```
	$ createdb -O <database-username> -U <database-creation-permission-username> <database-name>
	```	
10. Add (or change) the following properties in ```common.properties``` file under ```<PROJECT-ROOT>/paw-webapp/webapp/src/main/resources/config``` with the following information:

	``` properties
	db.host=<database-hostname>		# Should be "localhost" for local database
	db.port=<database-listening-port>	# Should be 5432 if using Postgresql default settings
	db.username=<database-username>		# Should match the username chosen in step 8 for local database
	db.name=<database-name>			# Should match the name chosen in step 9 for local database
	```	

### Build

1. Change working directory to ```<PROJECT-ROOT>/paw-webapp```

	```
	$ cd <PROJECT-ROOT>/paw-webapp
	```
2. Install project modules

	```
	$ mvn install
	```
3. Resolve dependencies

	```
	$ mvn dependency:resolve
	```
4. Build ```war``` file

	```
	$ mvn clean package
	```
	This file will be located under ```<PROJECT-ROOT>/paw-webapp/webapp/target```.


## Authors
* [Juan Marcos Bellini](https://github.com/juanmbellini)
* [Juan Li Puma](https://github.com/lipusal)
* [Diego de Rochebouët](https://github.com/Drocheg)
* [Julián Rodriguez Nicastro](https://github.com/julianrod94)