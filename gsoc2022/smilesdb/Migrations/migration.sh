
# echo '<ENTER PASSORD>' | openssl enc -aes-256-cbc -md sha512 -a -pbkdf2 -iter 100000 -salt -pass pass:'test@123'

# chmod 600 ./secret.txt

# echo 'mysecretpassword' | openssl enc -base64 -e -aes-256-cbc -md sha512 -a -pbkdf2 -iter 100000 -salt -pass pass:<ENTER PASSORD>  > .secret.lck

# <ENTER PASSORD> -> example: -pPass@123 


echo creating database
mysql -uroot -p<ENTER PASSORD> -e "create database OEstorage"

echo Restoring Database
mysql -h 127.0.0.1 -uroot -p<ENTER PASSORD> OEstorage < ./data/dump_29july2022.sql

echo updating rows to replace double-quote
mysql -h 127.0.0.1 -uroot -p<ENTER PASSORD> -DOEstorage < ./data/moleculesUpdate.sql

echo Generating JSON
mysql -h 127.0.0.1 -uroot -p<ENTER PASSORD> -DOEstorage < ./data/molecule.sql > ./data/molecule.temp1.json
# rm ./data/molecule.json

echo Replacing 'NULL' with ""
sed 's/null/""/g' ./data/molecule.temp1.json > ./data/molecule.temp2.json

echo Removing Header Line
awk 'NR>1'  ./data/molecule.temp2.json > ./data/molecule.json

echo Removing temp files
rm ./data/molecule.temp1.json data/molecule.temp2.json

echo Saving to mongo
cat ./data/molecule.json
mongo <  ./dropcollection.js
/opt/homebrew/bin/mongoimport --jsonArray --uri="mongodb://localhost:27017" --db=smilestest1 --collection=molecule --type=json ./data/molecule.json