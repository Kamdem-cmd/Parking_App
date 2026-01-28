# Parking_App
Ce projet vise la réalisation d'une application de gestion de parking pour véhicule en Java avec interface graphique intuitive 



# 🚗 Parking App 
Ce projet est une application Java avec interface graphique (GUI) permettant la gestion d'un parking. Il permet de gérer différents types de véhicules et de suivre les événements liés au stationnement.

## 📂 Structure du Projet
Le projet est organisé selon une architecture modulaire :
```bash
src/model/ : Contient les classes métiers (Vehicules, Types, etc.).

src/events/ : Gestion des événements du système.

src/ui/ : Interface utilisateur graphique.

src/Main.java : Point d'entrée de l'application.

bin/ : Fichiers compilés (.class).
```

## 🚀 Installation et Lancement
### Prérequis
Avoir le JDK (Java Development Kit) installé sur votre machine.

Un terminal ouvert à la racine du projet (Parking_App).

### 1. Lancement rapide (Version déjà compilée)
Si vous venez de cloner le dépôt et que le dossier bin est déjà présent avec les fichiers compilés, lancez simplement :

```Bash
java -cp bin Main
```

### 2. Compilation (Si modification du code)
Si vous modifiez les sources ou si vous souhaitez reconstruire le dossier bin, suivez ces étapes :

Créer le dossier de destination (si inexistant) :

```Bash
mkdir bin
```

Compiler tous les modules :

```Bash
javac -d bin src/model/*.java src/events/Event.java src/ui/UI.java src/Main.java
```

Lancer après compilation :

```Bash
java -cp bin Main
```

## 🛠️ Technologies utilisées
#### Java SE

#### Swing/AWT (pour l'interface graphique)

#### Programmation Orientée Objet