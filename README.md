# ZooKeeper

Ejemplos tomados del libro **Apache ZooKeeper Essentials** *[Aquí puedes conseguirlo](http://www.allitebooks.com)*

Este repo solo se centra en mostrar algunos ejemplos practicos citados en el libro de ZooKeeper.

# Herramientas utilizadas

  - [ZooKeeper](https://zookeeper.apache.org/), en este caso se utilizo la version 3.4.13
  - [Docker](https://www.docker.com/) como contenedor para las pruebas de concepto
  - El libro mencionado puedes encontrarlo en la pagina de [all it ebooks](http://www.allitebooks.com)

### Instalación

Para utilizar ZooKeeper puedes descargar la imagen de docker que he generado para las pruebas, o bien hacerlo en tu propia computadora.

```sh
$ docker pull spcruzaley/zookeeper
$ docker run -d -it --name zookeper-test spcruzaley/zookeeper:1 /bin/bash
$ docker exec -it zookeper-test /bin/bash
```

Una vez estando en el directorio de ZooKeeper, el cual de ahora en adelante llamaremos [ZK_HOME], (*Que comunmente es el directorio en el cual se encuentran las carpetas **bin, conf, docs, etc.***), levantamos el servicio.
### Ejecución
```sh
$ [ZK_HOME]/bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: [ZK_HOME]/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```

Veamos el estatus del servicio

```sh
$ [ZK_HOME]/bin/zkServer.sh status
ZooKeeper JMX enabled by default
Using config: [ZK_HOME]/bin/../conf/zoo.cfg
Mode: standalone
```

Listo, si todo salio bien, ya tenemos arriba nuestro servicio de ZooKeeper, el cual se ejecuta en el puerto 2181. Y solo para no dejar de lado, veamos si es correcto:

```sh
$ netstat -ntpl
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address       Foreign Address     State   PID/Program name
tcp        0      0 0.0.0.0:2181        0.0.0.0:*           LISTEN  1632/java
tcp        0      0 0.0.0.0:42865       0.0.0.0:*           LISTEN  1632/java
```

Como ven, ahi tenemos el **PID 1632** ejecutando a ZooKeeper en el **puerto 2181**

Procedemos a compilar el fuente para ejecutar los codigos de ejemplo.

`NOTA: Los ejemplos son básicos y, de acuerdo al libro, se recomienta utilizar Curator Framework para la interacción con ZooKeeper, ya que nos ofrece una interfaz mucho mas amigable para interactual con ZooKeeper (Ver ejemplo 5)`

```sh
$ git clone https://github.com/spcruzaley/zookeeper-examples.git
$ cd zookeeper-examples
$ mvn package
[INFO] Scanning for projects...
...
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5.658 s
[INFO] Finished at: 2019-03-29T23:24:10Z
[INFO] Final Memory: 23M/339M
[INFO] ------------------------------------------------------------------------
$ cd target
$ ./zookeeper
Must have some example as argument...
-----------------------------------------
usage: zookeeper [EXAMPLE NUMBER]

examples availables:
	[CHAPTER 3] example1
	[CHAPTER 3] example2-watcher, example2-updater
	[CHAPTER 3] example3-monitor <host:port>, example3-client <host:port>
	[CHAPTER 6] example4-curator-client
	[CHAPTER 6] example5-curator-framework

example: zookeeper example2-updater
```

### Ejecutando el ejemplo 1

```sh
$ ./zookeeper example1
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Znodes of '/':
zookeeper
```

Este ejemplo simplemente nos lista los **znodes** actuales.

### Ejecutando el ejemplo 2
El ejemplo 2 consta de un ***Watcher*** que se encargara de notificar cuando se agrega escribe informacion en el ***znode*** que estamos monitoreando, en este caso ***MyConfig***
## Ventana 1
```sh
$ ./zookeeper example2-watcher
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:null
Event Received: WatchedEvent state:SyncConnected type:NodeCreated path:/MyConfig
Current Data @ ZK Path /MyConfig:
```
## Ventana 2
```sh
$ ./zookeeper example2-updater
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Created/Updated: WatchedEvent state:SyncConnected type:None path:null
```
## Regresamos a la Ventana 1
```sh
$ ./zookeeper example2-watcher
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:null
Current Data @ ZK Path /MyConfig: 85f30717-5c90-4f52-ac28-50384cbac3f2^Croot@71a0cc5519ef:~/repos/zookeeper-examples/target# ./zookeeper example2-watcher
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:null
Event Received: WatchedEvent state:SyncConnected type:NodeCreated path:/MyConfig
Current Data @ ZK Path /MyConfig:
Event Received: WatchedEvent state:SyncConnected type:NodeDataChanged path:/MyConfig
Current Data @ ZK Path /MyConfig: aea2714a-7d88-406d-a224-c3a2d0fe1270
Event Received: WatchedEvent state:SyncConnected type:NodeDataChanged path:/MyConfig
Current Data @ ZK Path /MyConfig: abc903d3-f090-479c-8d34-54350d5d07ca
Event Received: WatchedEvent state:SyncConnected type:NodeDataChanged path:/MyConfig
Current Data @ ZK Path /MyConfig: d0927587-aa33-4eca-9ddb-e7bbdb65cb70
```
Como se puede ver, se registran los eventos y mostramos por pantalla la información enviada al znode *ejem: ***Current Data @ ZK Path /MyConfig: d0927587-aa33-4eca-9ddb-e7bbdb65cb70****

## Ejecutando el ejemplo 3 (1/2 - Monitor)
```sh
$ ./zookeeper example3-monitor localhost:2181
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:nullMembers: []
```
## Ejecutando el ejemplo 3 (2/2 - Cliente)
```sh
$ ./zookeeper example3-client localhost:2181
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:null
```
Despues de la ejecucion del cliente para generar un ***znode***, regresanmos a la ventana 1 para ver la notificación del evento:
```sh
Event Received: WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/Members
MESSAGE: !!!Cluster Membership Change!!!
MESSAGE: Members: [1938]
Event Received: WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/Members
MESSAGE: !!!Cluster Membership Change!!!
MESSAGE: Members: [1938, 6298]
Event Received: WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/Members
MESSAGE: !!!Cluster Membership Change!!!
MESSAGE: Members: [1938, 6298, 9102]
```
## Ejecutando el ejemplo 3 (1/2 - Monitor)
```sh
$ ./zookeeper example3-monitor localhost:2181
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Event Received: WatchedEvent state:SyncConnected type:None path:nullMembers: []
```
De esa manera se pueden ejecutar los demas ejemplos.
# Interfaz de Linea de Comandos (CLI)
**ZooKeeper** al igual que la mayoria de las herramientas cuenta con una interfaz para interactuar directamente con el. Veamos algunos comandos basicos.
```sh
$ cd [ZK_HOME]
$ ./zkCli.sh -server localhost:2181
Connecting to localhost:2181
...
...
[main-SendThread(localhost:2181):ClientCnxn$SendThread@1303] - Session establishment complete on server localhost/127.0.0.1:2181, sessionid = 0x1000883585d0008, negotiated timeout = 30000

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[zk: localhost:2181(CONNECTED) 0]
```
Una vez conectados a ZooKeeper, podemos:

**Listar todos los nodos actuales** *(ls <path>)*
```sh
[zk: localhost:2181(CONNECTED) 0] ls /
[zookeeper, MyConfig, Members]
```
**Crear un nodo con algun dato especifico** *(create <path> "Mensaje/Informacion")*
```sh
[zk: localhost:2181(CONNECTED) 2] create /unNodoMas "Con algo de informacion"
Created /unNodoMas
[zk: localhost:2181(CONNECTED) 3] ls /
[zookeeper, MyConfig, Members, unNodoMas]
```
**Crear un hijo** *(create <path> "Mensaje/Informacion")*
```sh
[zk: localhost:2181(CONNECTED) 4] create /unNodoMas/unHijo "Informacion de mi hijo"
Created /unNodoMas/unHijo
[zk: localhost:2181(CONNECTED) 5] ls /unNodoMas
[unHijo]
[zk: localhost:2181(CONNECTED) 6]
```
**Ver la informacion de un nodo** *(get <path>)*
```sh
[zk: localhost:2181(CONNECTED) 6] get /unNodoMas
Con algo de informacion
cZxid = 0x151
ctime = Sat Mar 30 01:08:57 GMT 2019
mZxid = 0x151
mtime = Sat Mar 30 01:08:57 GMT 2019
pZxid = 0x152
cversion = 1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 23
numChildren = 1
```
*La informacion de cada uno de los ***fields*** se describe a detalle en el libro citado al principio del README.*

**Eliminar un nodo** *(delete <path>)*
```sh
[zk: localhost:2181(CONNECTED) 13] ls /
[zookeeper, MyConfig, Members, unNodoMas]
[zk: localhost:2181(CONNECTED) 14] delete /MyConfig
[zk: localhost:2181(CONNECTED) 15] ls /
[zookeeper, Members, unNodoMas]
[zk: localhost:2181(CONNECTED) 16]
```
Si se requiere consultar todas las operaciones que se pueden realizar desde el CLI, como siempre solo basta con escribir ***help***
```sh
[zk: localhost:2181(CONNECTED) 16] help
ZooKeeper -server host:port cmd args
	stat path [watch]
	set path data [version]
	ls path [watch]
	delquota [-n|-b] path
	ls2 path [watch]
	setAcl path acl
	setquota -n|-b val path
	history
	redo cmdno
	printwatches on|off
	delete path [version]
	sync path
	listquota path
	rmr path
	get path [watch]
	create [-s] [-e] path data acl
	addauth scheme auth
	quit
	getAcl path
	close
	connect host:port
[zk: localhost:2181(CONNECTED) 17]
```
Si bien esto no se usa dia a dia en ambientes productivos de *manera manual*, es bueno conocer un poco su funcionamiento.
Esto y mas se describe en el libro citado al principio del README el cual recomiendo ampliamente su lectura para conocer a detalle las caracteristicas de este proyecto.

## SPCruzaley
