## Deployment
when deploy the project(docker or whatever), pls configure the JVM setting, 
the default setting for heap is 512MB, you can change the size. 
## IDEA change memory to prevent something terrible
search 'memory', then click change memory setting, For arch 4096MB is 
enough(maybe)
## Postgresql configuration:
enter postgresql: <br/>&nbsp;&nbsp;&nbsp;&nbsp;
sudo su - postgres <br/>&nbsp;&nbsp;&nbsp;&nbsp;
psql<br/>&nbsp;&nbsp;&nbsp;&nbsp;
default username:postgres<br/>&nbsp;&nbsp;&nbsp;&nbsp;
\c database_name
<br/><br/>
## Redis configuration:
First we have to install redis.

sudo pacman -S redis

Next we have to enable it.

sudo systemctl enable redis.service

Let's start the redis server.

redis-server

Check the connection.

redis-cli ping // if you get PONG, its working

Stop the server.

press "CTRL + C" on the same terminal

OR

sudo systemctl stop redis.service
