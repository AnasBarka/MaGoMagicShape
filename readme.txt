Un Tamagotchi possède deux attributs, une valeur de faim et une valeur de soif, initialement toutes deux à 100. Tant que ces valeurs sont supérieures à zéro, le Tamagotchi est considéré comme vivant.

Ces valeurs diminuent toutes les secondes selon une valeur aléatoire définie lors de l'instanciation du Tamagotchi. La fenêtre de l'interface graphique JavaFX contient deux boutons centrés qui affichent les points restants de faim et de soif. L'utilisateur peut augmenter ces valeurs en cliquant sur le bouton correspondant. Lorsque l'état du Tamagotchi n'est plus vivant, l'application se termine.

Le programme est architecturé selon les design patterns MVC (Modèle-Vue-Contrôleur) et Observer. Le contrôleur est responsable de la gestion des événements provenant de la vue, tandis que l'Observer est responsable de mettre à jour l'interface graphique en fonction des changements dans le modèle.


Voici un **résumé clair et structuré** de tout ce que nous avons fait depuis le début de ton projet JavaFX basé sur le modèle **MVC** autour des objets **MaGo** :

---

## 🎯 Objectif du projet

Créer une application JavaFX dans laquelle plusieurs objets appelés **MaGo** (ex. `CircleMaGo`, `SquareMaGo`) analysent un message, réagissent avec un état émotionnel, et où la **vue** affiche ces états ainsi qu’une **moyenne générale** calculée dynamiquement.

---

## 🧠 Architecture MVC mise en place

### ✅ **Modèle (Model)**

Contient la logique métier :

* **Classe `MaGo` (abstraite)** : représente un magicien avec un état émotionnel.
* **Sous-classes `CircleMaGo`, `SquareMaGo`** : comportement spécifique de chaque type de MaGo.
* **Interface `MaGoObserver`** : permet à la vue d'être notifiée lorsque le modèle change.
* **Méthode `notifyObservers(...)`** dans `MaGo` : appelée après chaque changement d’état émotionnel.

### ✅ **Vue (View) : `MaGoView`**

* Gère uniquement **l’affichage graphique**.
* Observe les objets `MaGo` (implémente `MaGoObserver`).
* Met à jour dynamiquement :

  * L’état émotionnel de chaque MaGo.
  * La **moyenne générale** des émotions en appelant le **contrôleur**.
* Utilise `Platform.runLater(...)` pour mettre à jour l’interface depuis un autre thread.

### ✅ **Contrôleur (Controller) : `MaGoController`**

* Reçoit la vue et la liste de MaGo.
* Démarre un **compte à rebours de 10 secondes** avec `Timeline`.
* Gère :

  * Le clic sur le bouton pour envoyer un message.
  * L’appel à la méthode `analyzeMessage(...)` sur chaque MaGo.
  * Le **calcul de la moyenne** des états émotionnels → utilisé par la vue.

---

## 🔄 Communication entre composants

* La **vue observe** les `MaGo` via l'interface `MaGoObserver`.
* Lorsqu’un MaGo change, il appelle `notifyObservers(...)`.
* La vue est notifiée, met à jour l’affichage du MaGo, et **appelle une méthode du contrôleur** pour recalculer la moyenne.
* La vue **n’effectue aucun calcul** par elle-même → elle **demande au contrôleur** la valeur.

---

## 🛠️ Autres fichiers/supports

### 📁 `MaGoFactory`

* Génère et retourne une liste de `MaGo` (ex: 5 cercles, 5 carrés).

### 🟦 `Main.java`

* Crée :

  1. Les objets `MaGo`.
  2. La vue (sans le contrôleur).
  3. Le contrôleur (relié à la vue et au modèle).
  4. Associe la vue au contrôleur via `setController(...)`.
  5. Enregistre la vue comme **observateur** des `MaGo`.

---

## 🧪 Modifications importantes faites

| Élément          | Modification effectuée                                          |
| ---------------- | --------------------------------------------------------------- |
| `MaGo`           | Ajout d’un observer (liste d’observateurs)                      |
| `MaGoObserver`   | Interface pour observer les changements                         |
| `MaGoView`       | Affiche les états et moyenne, observe les MaGo                  |
| `MaGoController` | Ajout de `calculateAverageEmotion()`                            |
| `Main.java`      | Correction de l’ordre d’instanciation, ajout de `setController` |
| Vue              | N’affiche que, **ne calcule rien** directement                  |
| Contrôleur       | Reçoit les événements de la vue et **contrôle la logique**      |

---

## ✅ Pattern MVC : Respecté ?

**Oui, pleinement respecté.**

| Rôle       | Responsabilité principale                           |
| ---------- | --------------------------------------------------- |
| Modèle     | Gère les données et la logique métier               |
| Vue        | Affiche les données et interagit avec l’utilisateur |
| Contrôleur | Fait le lien entre la Vue et le Modèle              |

---

Souhaites-tu que je te génère un **diagramme de classes UML**, un **diagramme de flux MVC**, ou que je t’aide à faire une **présentation PowerPoint ou PDF** de ton projet ?


-******************************************

non c'est bon j'ai reussi:
Travail demandé :
Pour ce projet, vous allez devoir implémenter une application JavaFX en
appliquant les concepts vus en cours tel que la conception pilotée par les
responsabilités et l'application adéquate de design patterns.
Le projet devra être développé individuellement.Introduction :
La société HELBro souhaite expérimenter un jeu de gestion dans lequel de petites
créatures virtuelles, appelées MaGo, réagissent aux messages publiés par un
groupe d'utilisateurs issus d'un réseau social. L'objectif est d'observer les effets
positifs ou négatifs de ces interactions sur le comportement des MaGo.
Description du problème :
Les MaGos sont de petites créatures possédant un statut représentant leur niveau
de bonheur. Il s'agit d'une valeur dans l'intervalle [-100, 100], où -100 correspond
au niveau de bonheur minimal (tristesse extrême) et 100 au niveau maximal
(bonheur extrême).
Les MaGos réagissent aux messages envoyés par des utilisateurs. Chaque type
de MaGo possède son propre comportement face aux messages, ce qui entraîne
une variation de son statut individuel.
Il vous est donc demandé de développer un programme permettant de visualiser
les effets des messages sur une population de MaGos. L'interface principale de ce
programme est illustrée dans la maquette ci-dessous.

Average Status
Negative Color
Positive Color
5
Random
This is a message
GoCirle
GoSquare
GoAny

L'interface principale se divise en trois sections horizontales, appelées la partie
haute, la partie centrale et la partie basse.
La partie haute de l'interface contient trois éléments. Le premier, situé à gauche,
est un indicateur dynamique affichant l'état moyen actuel de tous les MaGos. Par
exemple, pour une population de trois MaGos dont deux sont extrêmement
heureux et un extrêmement triste, l'état moyen correspondra à la moyenne de
leurs valeurs, ce qui donnera un résultat global plutôt positif.
Les deux autres éléments, placés à droite de l'interface, sont des labels indiquant
les couleurs de référence : le bleu pour la tristesse maximale et le rouge pour le
bonheur maximal. Comme mentionné précédemment, l'état des MaGos - et donc
leur couleur - évoluera entre ces deux extrêmes tout au long de l'exécution du
programme.
Le schéma suivant illustre les variations attendues d'état, et donc, de couleur :
0,0,255
127,127,255
191,191,255 255,255,255
255,191,191
255,127,127
255,0,0
0000FF
7F7FFF
BFBFFF
FFFFFF
FFBFBF
FF7F7F
FF0000
TMin
TMin/2
TMin/4
0
TMax/4
TMax/2
TMax

De gauche à droite, on observe une progression de l'état, allant de la valeur de
tristesse maximale (ici nommée TMin) à la valeur de bonheur maximal (ici nommée
TMax). L'état neutre, situé au centre, est représenté en blanc. Il est important de
noter que chaque état doit être associé à une couleur distincte. Ainsi, même si la
différence n'est pas toujours perceptible à l'œil nu pour l'utilisateur, l'état +36 doit
avoir une couleur différente de celle de l'état +37.
La partie basse de l'interface permet de visualiser les MaGos. Leur forme permet
de distinguer leur type, tandis que leur couleur reflète leur état. Dans cette
maquette, on observe une population de deux MaGos de types différents : celui de
gauche est plutôt heureux, celui de droite plutôt triste.
Cette partie de l'interface doit être responsive, c'est-à-dire que la taille des MaGos
doit s'adapter au nombre total de créatures afin de s'ajuster à la taille de la fenêtre.
En d'autres termes, plus la population est nombreuse, plus les MaGos seront
affichés en petit dans la partie basse pour rester visibles dans la fenêtre.
Enfin, la partie centrale de l'interface permet de visualiser les messages et de
contrôler leur défilement. Tout à gauche, une zone de texte accompagnée d'une
barre de défilement affiche le message actuel auquel réagissent les MaGos. Si le
texte est trop long, la barre permet d'en consulter la suite.
Juste à droite de cette zone, deux éléments sont disposés verticalement. En haut,
un compteur dynamique indique le temps restant avant l'apparition du prochain
message. Quand celui-ci arrive à zéro, un nouveau message est automatiquement
affiché. En dessous, un bouton affichant un symbole de flèche « -> » permet de
forcer l'affichage immédiat du message suivant, réinitialisant ainsi le compteur.
Chaque fois qu'un nouveau message apparaît, les MaGos y réagissent
immédiatement, ce qui modifie leur état. Toujours dans la partie centrale, à
l'extrême droite, une zone composée de boutons radio disposés verticalement
permet de sélectionner le mode de défilement. Ce choix influence la stratégie de
selection du message suivant. Quatre modes de defilement sont actuellement
disponibles et seront décrits plus en détail dans la suite de ce document.
Types de MaGos et réactions aux messages :
Il existe actuellement deux types de MaGos : le rond (Circle) et le carré (Square).
Ces formes sont également celles qui doivent apparaître dans l'interface principale
pour les représenter visuellement.
Chaque type de MaGo réagit différemment aux messages.
Le MaGo de type Square analyse le ratio voyelles/consonnes du message. Si ce
ratio est supérieur à une valeur pivot fixée à 0,89, le niveau de bonheur du MaGo
augmente de 10% de la valeur maximale de bonheur. À l'inverse, si le ratio est
inférieur à cette valeur pivot, le bonheur diminue de 10% de cette même valeur
maximale.
Par exemple, si la valeur maximale de bonheur est de 200, et qu'un MaGo de type
Square a un bonheur actuel de -50, alors, pour le message : « Nicolas Flamel est
le seul à avoir fabriqué la pierre philosophale. - La quoi ?! », on compte 30
voyelles et 32 consonnes, soit un ratio de 30 / 32 = 0,94. Comme 0,94 > 0,89, le
bonheur du MaGo augmente de 10% de 200, soit 20 unités. Le niveau de bonheur
passe donc de -50 à -30.
Le MaGo de type Circle réagit à la ponctuation présente dans le message. Pour
chaque occurrence des symboles « ! » ou « ? », son niveau de bonheur augmente
de 5% de la valeur maximale de bonheur. En revanche, si aucun de ces symboles
n'apparaît dans le message, son niveau de bonheur diminue de 10 % de cette
valeur maximale.
Par exemple, Si la valeur maximale de bonheur est de 200 et que le MaGo de type
Circle a un niveau de bonheur actuel de 40, deux cas peuvent se présenter. Dans
le premier cas, le message contient de la ponctuation pertinente : « Nicolas Flamel
est le seul à avoir fabriqué la pierre philosophale. - La quoi ?! ».
Ce message comporte deux symboles considérés comme positifs (« ? » et « ! »),
ce qui entraîne une augmentation de 10% de la valeur maximale de bonheur, soit
+20 unités. Le niveau de bonheur du MaGo passe alors de 40 à 60.
Dans le second cas, le message ne contient ni point d'exclamation ni point
d'interrogation, comme dans : « Nicolas Flamel a fabriqué la pierre philosophale
selon la légende ». En l'absence de ces symboles, le bonheur diminue de 10% de
la valeur maximale, soit 20 unités, et passe donc de 40 à 20.



oui mais je prefere ne pas utiliser des inctenseoff je peux pas juste 
rajouter quelque choose dans la classe eneft une varaible form 

 // Couleur allant de bleu (triste) à rouge (heureux), blanc au centre
    private static Color getColorForBonheur(int bonheur) {
        double t = (bonheur + 100) / 200.0;  // t ∈ [0, 1]
        int r = (int) (255 * t);
        int g = (int) (255 * (1 - Math.abs(t - 0.5) * 2)); // blanc au centre
        int b = (int) (255 * (1 - t));

        return Color.rgb(clamp(r), clamp(g), clamp(b));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    @Override
    public Node getView(double size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setFill(getColorForBonheur(bonheur));
        return rect;
    }

    @Override
    public Node getView(double size) {
        Circle circle = new Circle(size / 2);
        circle.setFill(getColorForBonheur(bonheur));
        return circle;
    }