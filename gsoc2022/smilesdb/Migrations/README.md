# Migration Schema

This includes the shell script to migrate the data from MySQL to MongoDB which are hosted on local server with default ports.

**SQL Details**<br>
Save the OEstorage in local server.
* Considered Database: OEstorage
* Table: molecule

**MongoDB Details** <br>
Configure the database with the fields mentioned in migrate.sh<br>

* Database name: smilestest1
* Collection name: molecule

```commandline
mongo
use smilestest1
db.createCollection("molecule")
```