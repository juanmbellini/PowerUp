#!/bin/bash
POSTGRES_SERVER_STDOUT_LOG="postgres_server_stdout.log"
POSTGRES_SERVER_STDERR_LOG="postgres_server_stderr.log"
PSQL_STDOUT_LOG="psql_stdout.log"
PSQL_STDERR_LOG="psql_stderr.log"
DEV_USER="paw_dev"
TEST_USER="paw_test"
DEV_DATABASE="pawDB_dev"
TEST_DATABASE="pawDB_test"
USERS_PASSWORD="LaPassWordGuaching"
TABLES_FILE="pawDB.sql"

# Function to create users, if they don't exist
# First argument is username, and second one, user password
function create_user {
	psql postgres -tAc "SELECT 1 FROM pg_roles WHERE rolname='$1';" | grep -q 1 || createuser -d $1
	psql postgres -tAc "ALTER USER $1 WITH PASSWORD '$2';"
}
# Function to create databases, if they don't exist
# First argument is database name, and second one, user who creates it
function create_db {
	psql -lqt | cut -d\| -f 1 | grep -wq $1 || createdb $1 -U $2
}
# Function to stop database server
# Must be called after script execution
function exit_function {
	echo -n "Stopping database server... "
	kill -2 $!
	echo "Done"
}
# Function to report error and abort execution of script
function error_function {
	echo "Error"
	echo "Something went wrong... Aborting"
	exit
}
# Function that initializes database server, and creates users, databases, and tables
# Returns 0 on sucess, or -1 otherwise
function initialize_databases {
	# Initializes database server in background, logging into corresponding files
	echo -n "Starting database server... "
	postgres > $POSTGRES_SERVER_STDOUT_LOG 2> $POSTGRES_SERVER_STDERR_LOG -D /usr/local/var/postgres &
	echo "Done"

	sleep 1	# Waits for database server to initialize

	# Creates users, databases, and tables, logging into corresponding files
	echo -n "Creating users, databases, and tables... "
	create_user $DEV_USER $USERS_PASSWORD > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	create_user $TEST_USER $USERS_PASSWORD > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	create_db $DEV_DATABASE $DEV_USER > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	create_db $TEST_DATABASE $TEST_USER > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	psql $DEV_DATABASE -U $DEV_USER -f $TABLES_FILE > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	psql $TEST_DATABASE -U $TEST_USER -f $TABLES_FILE > $PSQL_STDOUT_LOG 2> $PSQL_STDERR_LOG || return 1
	echo "Done"
	echo "Databases have been initialized!"
	return 0
}

# Script starts here ...

set -e # Aborts script if anything goes wrong
trap exit_function EXIT INT QUIT KILL TERM STOP
initialize_databases || error_function

