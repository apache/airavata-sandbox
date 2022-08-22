
# echo 'b1h3a1v1e4s2h1' | openssl enc -aes-256-cbc -md sha512 -a -pbkdf2 -iter 100000 -salt -pass pass:'test@123'

# chmod 600 ./secret.txt

# echo 'mysecretpassword' | openssl enc -base64 -e -aes-256-cbc -md sha512 -a -pbkdf2 -iter 100000 -salt -pass pass:b1h3a1v1e4s2h1  > .secret.lck

# b1h3a1v1e4s2h1


echo creating database
mysql -uroot -pb1h3a1v1e4s2h1 -e "create database OEstorage"

echo Restoring Database
mysql -h 127.0.0.1 -uroot -pb1h3a1v1e4s2h1 OEstorage < ./data/dump_29july2022.sql

echo updating rows to replace double-quote
mysql -h 127.0.0.1 -uroot -pb1h3a1v1e4s2h1 -DOEstorage < ./data/moleculesUpdate.sql

echo Generating JSON
mysql -h 127.0.0.1 -uroot -pb1h3a1v1e4s2h1 -DOEstorage < ./data/molecule.sql > ./data/molecule.temp1.json
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