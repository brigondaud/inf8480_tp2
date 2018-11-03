# INF8480 - TP2 - Services distribués et gestion des pannes

Ce README détaille les étapes pour pouvoir exécuter le système.

## Prise en main

### Prérequis

Installer [Apache Maven](https://maven.apache.org/install.html).

### Installation

Pour compiler le projet et les exécutables pour lancer le service de répertoire de noms, les serveurs de calcul et le répartiteur:
```
mvn package
```

## Exécution des tests

Pour exécuter les tests:
```
mvn test
```

## Utilisation

Avancer de pouvoir lancer les serveurs, après avoir compilé le projet, lancer Java RMI à la racine du projet en éxecutant:
```
rmiregistry -J-Djava.rmi.server.codebase=file:target/tp2.jar numero_port &
```

Dans un premier temps, lancer le serveur de répertoire de noms:
```
./directory.sh [options]
```

Puis lancer le/les serveur(s) de calcul:
```
./server.sh [options]
```

### OPTIONS
```
--unsafe (server, specify that the server is corrupted)
--corrupt rate (server, specify the corrupt rate of the server, must be between 0 and 100)
--ipDir ip ([server, repartitor], specify the IP address of the name directory)
--portDir port ([server, directory], specify on which port the name directory should be/is running)
--portServer port (server, specify on which port the server should be running)
--capacity C (server, specify the capacity of the compute server)
--operations file_path (repartitor, Specify the RELATIVE file path of the file containing the operations)
```


## Développé avec

* [Apache Maven](https://maven.apache.org/)
* [JUnit](https://junit.org/junit4/)

## Auteurs

* **Baptiste Rigondaud (1973586)**
* **Loïc Poncet (1973621)**

