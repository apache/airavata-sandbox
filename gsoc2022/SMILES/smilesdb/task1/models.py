from django.db import models
from pymongo import *


def db_client(port):

    try:
        client = MongoClient('localhost', port)
        print("Successfully connected to MongoDB at port {}".format(port))

        show_dbs = client.list_database_names()

        for i in sorted(show_dbs):
            print(i)
        
    except:
        print("Error in connecting to Database")

    return client

def connect_db(port, db_name, coll_name):
    client = db_client(port)
    try:
        connect_db.workdb = client[db_name]
        connect_db.dbcoll = connect_db.workdb[coll_name]
        print(f"Sucessfully Configured to {db_name}/{coll_name}")
    except:
        print('Configuration error, Unable to connect.')

    # return db_name, dbcoll


connect_db(27017,'SMILESdb', 'task1')



# workdb = client['SMILESdb']
# mycoll = workdb['task1']

# record = {
#     "university" : "Guru Nanak University",
#     }

# add_rec = connect_db.dbcoll.insert_one(record)
# rm_rec = mycoll.delete_one(record)
# Create your models here.
