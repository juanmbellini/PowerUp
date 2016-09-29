#!/bin/bash

set -e # Aborts script if anything goes wrong

psql postgres -tAc "SELECT 1 FROM pg_roles WHERE rolname='paw';" | grep -q 1 || createuser -d paw;
psql postgres -tAc "ALTER USER paw WITH PASSWORD 'paw';";
psql -lqt | cut -d\| -f 1 | grep -wq paw || createdb paw -U paw
