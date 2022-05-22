"""
This script allows you to upload a csv file to your app's firestore database as a collection
There a few requirements regarding the csv:
- the first attribute should be a unique identified (key)
- the first line of the csv should contain the name of the collection you want to create/update
- the second line should contain the attributes of each tuple
- the third line should contain the data types of the attributes
- the actual data starts at the 4th line
- lines with no key will not be written
"""

import csv
import enum
import os
import firebase_admin
from firebase_admin import credentials, firestore

# modify this with your own files
CSV_FILE_PATH = 'course_list.csv'
CERTIFICATE_PATH = 'ratemyepfl-firebase-adminsdk-he1ed-c6d62b7650.json'

# You need to set the environment variable in order for the script to work
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = CERTIFICATE_PATH

cred = credentials.Certificate(CERTIFICATE_PATH)
app = firebase_admin.initialize_app(cred)

DB = firestore.Client()

collection_name = ''
header = []
data_types = []

# poorly optimized for now, database writes should be batched
with open(CSV_FILE_PATH, 'r') as csv_file:
    reader = csv.reader(csv_file, delimiter=',')
    collection_name = "courses"
    header = next(reader)[1:]
    data_types = next(reader)[1:]

    print(f"collection_name {collection_name}")
    print(f"header: {header}")
    print(f"data type: {data_types}")
    
    for row in reader:
        tuple = {}
        key = row.pop(0)
        if key:
            for index, attribute in enumerate(row):
                if attribute:
                    if data_types[index] == 'int':
                        tuple[header[index]] = int(attribute)
                    elif data_types[index] == 'float':
                        tuple[header[index]] = float(attribute)
                    else:
                        tuple[header[index]] = attribute
                DB.collection(collection_name).document(key).set(tuple)
                print('Added ' + key)

csv_file.close()