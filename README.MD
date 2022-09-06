enter postgresql: <br/>&nbsp;&nbsp;&nbsp;&nbsp;
sudo su - postgres <br/>&nbsp;&nbsp;&nbsp;&nbsp;
psql<br/>&nbsp;&nbsp;&nbsp;&nbsp;
default username:postgres<br/>&nbsp;&nbsp;&nbsp;&nbsp;
\c database_name
<br/><br/>
Redis configuration:<br/>
First we have to install redis.

sudo pacman -S redis

Next we have to enable it.

sudo systemctl enable redis.service

Lets start the redis server.

redis-server

Check the connection.

redis-cli ping // if you get PONG, its working

Stop the server.

press "CTRL + C" on the same terminal

OR

sudo systemctl stop redis.service
