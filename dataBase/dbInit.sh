#!/bin/bash
STDOUT_LOG="stdout.log"
STDERR_LOG="stderr.log"
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


set -e # Aborts script if anything goes wrong

# Initializes database server in background, logging into files named as STDOUT_LOG and STDERR_LOG
echo -n "Starting database server..."
postgres > $STDOUT_LOG 2> $STDERR_LOG -D /usr/local/var/postgres &
echo "[Done]"

sleep 1	# Waits for database server to initialize

# Creates users, databases, and tables
echo -n "Creating users, databases, and tables..."
create_user $DEV_USER $USERS_PASSWORD
create_user $TEST_USER $USERS_PASSWORD
create_db $DEV_DATABASE $DEV_USER
create_db $TEST_DATABASE $TEST_USER
psql $DEV_DATABASE -U $DEV_USER -f $TABLES_FILE
psql $TEST_DATABASE -U $TEST_USER -f $TABLES_FILE
echo "[Done]"

echo "Finishing database server..."
kill -2 $!
echo "[Done]"

echo "Databases have been initialized!"