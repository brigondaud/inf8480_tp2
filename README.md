# INF8480 - TP2 - Services distribués et gestion des pannes

Ce README détaille les étapes pour pouvoir exécuter le système de calcul distribué .

## Prise en main

### Prérequis

Installer [Apache Maven](https://maven.apache.org/install.html).

### Installation

Pour compiler le projet et les exécutables et lancer les tests :
```
mvn package
```

Pour simplement compiler le projet et les exécutables :
```
mvn package -Dmaven.test.skip=true
```

## Exécution des tests

Pour exécuter les tests:
```
mvn test
```

Les tests comportent des tests unitaires ainsi que des tests d'intégration en mode sécurisé et non-sécurisé. Les tests seront exécutés pour tous les fichiers se situant dans le répertoire:
```
operations/
```

***Important***: Les configurations des serveurs de calcul malicieux peuvent comporter des taux de corruption élevés dans les jeux de tests. Leur temps d'exécution peut donc être très variable.

### Ajout de fichiers de tests

Un fichier de test comporte un nom au format:
```
operation-<result>
```
Où *result* désigne le résultat de la série d'opérations décrite dans le fichier de test. Celle-ci comporte des opérations du type:
```
prime <parameter>
pell <parameter>
```

## Utilisation

### Déploiement du répertoire de nom

Avant de pouvoir déployer les serveurs, après avoir compilé le projet, démarrer un rmiregistry sur un port disponible sur votre machine en exécutant à la racine du projet :
```
rmiregistry -J-Djava.rmi.server.codebase=file:target/tp2.jar numero_port &
```

Exécuter ensuite la commande suivante afin de lancer le répertoire de nom :
```
./directory.sh --portDir <port>
```

#### Options du répertoire de nom
```
--portDir port (Spécifie le numéro de port sur lequel le rmiregistry démarré précédemment est en train de s'exécuter.)
```
Il est important de noter que le port ayant pour numéro la valeur immédiatement supérieure à celle utilisée par le rmiregistry sera désormais utilisé sur la machine courante.

### Déploiement des serveurs de calcul

Après avoir déployé le service de répertoire de nom, il est possible de lancer un ou plusieurs serveurs en lancant pour chacun d'eux un rmiregistry sur un port non utilisé :
```
rmiregistry -J-Djava.rmi.server.codebase=file:target/tp2.jar numero_port &
```
Puis en exécutant la commande suivante :
```
./server.sh [--unsafe] [--corrupt <taux>] --ipDir <ip> --portDir <port> --portServer <port> --capacity <C>
```

#### Options des serveurs de calcul
```
--unsafe (Spécifie que le serveur est malicieux, doit être accompagné de l'option --corrupt)
--corrupt rate (Un nombre flottant entre 0 et 100 spécifiant le taux de corruption du serveur)
--ipDir ip (L'adresse IP de la machine sur laquelle le service de répertoire de nom s'exécute)
--portDir port (Le port sur lequel le rmiregistry du service de répertoire de nom s'exécute)
--portServer port (Le port sur lequel le rmiregistry du serveur que l'on s'apprête à lancer s'exécute)
--capacity C (Spécifie la capacité du serveur)
```
Comme précédemment, le port ayant pour numéro la valeur immédiatement supérieure à celle utilisée par le rmiregistry du serveur ne sera plus disponible sur la machine courante.

### Exécution du répartiteur

Pour lancer le répartiteur sur un fichier d'opérations, exécuter la commande suivante :
```
./repartitor.sh [--unsafe] --ipDir <ip> --portDir <port> --operations <file_path>
```

#### Options du répartiteur
```
--unsafe (Spécifie que le répartiteur doit vérifier ses résultats)
--ipDir ip (L'adresse IP de la machine sur laquelle le service de répertoire de nom s'exécute)
--portDir port (Le port sur lequel le rmiregistry du service de répertoire de nom s'exécute)
--operations file_path (Le chemin RELATIF vers le fichier contenant les opérations)
```

## Développé avec

* [Apache Maven](https://maven.apache.org/)
* [JUnit](https://junit.org/junit4/)

## Auteurs

* **Baptiste Rigondaud (1973586)**
* **Loïc Poncet (1973621)**

