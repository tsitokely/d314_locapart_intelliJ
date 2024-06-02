<p align="center"><img src="assets\d314-logo.svg" width="150"></p> 
<h2 align="center"><b>D314 - Locapart - Service</b></h2>
<h4 align="center">Application service pour le projet D314 Locapart.</h4>


<hr>

## <a id="description"></a>Description

L'application est un service JAX-RS où l'on peut louer des appartements dans des villes de France.
L'application a été pensé pour le client suivant: [D314 locapart client](https://github.com/tsitokely/d314-frontend).
Toutefois, étant donné que le service utilise JSON comme format de message, toute application cliente utilisant ce format est compatible.


### <a id="features"></a>Fonctionnalités

* Lister les villes disponibles sur le service (villes non ajoutables depuis le front-end)
* Lister les appartements disponibles sur le service (appartements non ajoutables depuis le front-end)
* Lister, créer, modifier et supprimer les réservations faites sur le service

## <a id="installation"></a>Installations et mise à jour
### Environnement requis - développement
* Apache Tomcat 10.1.18
* OpenJDK 12.0.2
* SQLite 3
* Maven 3.9.5

### Configuration requis pour le développement
1. Avant toute installation, veuillez vous asssurer TOMCAT, OpenJDK et SQLite3 sont installés. De plus, veuillez configurer votre IDE en conséquence
2. Après avoir pris le repo, allez dans le dossier `InstallScripts`, puis lancez la commande suivante dans un terminal ou un cmd (script.cmd disponible dans le dossier)
```sh
sqlite3 WsLocaPart.db ".read sqlite_script.sql"
```
Cette étape permettra de lancer le processus de configuration de la BD.
Après cette étape, vous devriez avoir le fichier `WsLocaPart.db` dans le dossier `InstallScripts`
3. Copier le fichier `WsLocaPart.db` du dossier `InstallScripts` vers le dossier `src/main/webapp/WEB-INF/classes`. En principe, ce fichier devrait déjà existé mais vous pouvez l'écraser
4. Dans votre IDE, lancer le build du projet et créer les artefacts `.war` et/ou `war exploded` selon votre choix de développement
5. Configurez Tomcat, puis, déployer le ou les artefacts projet choisi dans l'étape précedente sur le serveur Tomcat.
* Selon vos paramètres Tomcat, l'application se lancera sur un port et adresse réseau spécifique - notez cette adresse car vous en aurez besoin pour connecter l'application cliente au service 
* A ce stade, le service devrait fonctionner et l'URI de base du service est accessible via l'endpoint `/api`
* URI de base: `http://hostname:port/artefact/api`

### Notes sur le développement
Pour chaque modification dans le code source de l'application, un rebuild du projet et un redéploiement sur Tomcat sera nécessaire
Exemple (en utilisant )
```sh
npm run dev
```

### Installation en production
Pour la mise en production, Il suffit de déployer l'artefact `.war` sur le serveur Tomcat de production.

## Contribution
Que vous ayez des idées, des traductions, des changements de conception, du nettoyage de code ou de véritables changements de code, l'aide est toujours la bienvenue.
Plus on en fait, mieux c'est !

Si vous souhaitez vous impliquer, envoyez-moi un message.

## License

"Locapart - service" is an application that allows you to rent apartments through the call of a specific REST service
Copyright (C) 2024 Tsitohaina Toetra Rakotondramasy

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

The application is a free Software: You can use, study share and improve it at yourwill. Specifically you can redistribute and/or modify it under the terms of the [GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.  
