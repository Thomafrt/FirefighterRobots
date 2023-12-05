# FirefighterRobots
Système d’exploration autonome par robots en essaim pour la gestion d’incendies.


Les code est présent dans le dossier src du projet.
Les fichiers compilés sont présents dans le dossier bin.


Pour lancer l'application, exécutez la commande suivante à la racine : java -cp bin Main    

Une fois l'application lancée, des fenêtres modales vont s'ouvrir successivement afin de saisir tous les paramètres voulus (les valeurs par défaut sont déja entrées dans les champs):
 - le nombre de robots
 - le temps d'attente entre chaque tour (plus il est faible, plus les tours s'enchainent vite)
 - la probabilté qu'une case prenne feu si elle est adjacente à une case en feu
 - la probabilité qu'une case en feu s'éteigne
 - la dimension des côtés de la grille
 - le nombre d'humains à placer aléatoirement sur la grille
 - le nombre de départs de feu placés aléatoirement sur la grille

 Une fois tous les paramètres complétés, la grille apparait et la programme s'execute. 
 Les nombre de tours s'affiche dans la console. 
 Une fois tous les feux éteints, le nombre de tours total, le nombre de cases sauvés et le nombre d'humains sauvés s'affiche dans la console.


Pour compiler le projet entier après une modification, lancez la commande suivante à la racine : javac -d bin src/*.java