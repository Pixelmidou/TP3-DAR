# Serveur Multi-threads Client/Serveur en Java — Sockets TCP (TP3)

## Description

Ce projet prolonge les applications développées dans les TP1 et TP2 en transformant le serveur en un **véritable serveur multi-thread**, capable de gérer plusieurs clients **simultanément**. Chaque client connecté est traité dans un **thread dédié**, ce qui permet au serveur d'accepter de nouvelles connexions sans attendre la fin du traitement du client précédent. Le projet est organisé en deux activités : **3‑1** (acceptation de plusieurs clients) et **3‑2** (calculatrice multi-thread avec échange d'objets et compteur synchronisé).

## Structure du projet

```
src/
├── clientPackage/
│   ├── Client_3_1.java          → envoie l'opération sous forme de CC
│   └── Client_3_2.java          → envoie un objet Operation
├── serverPackage/
│   ├── ServerMT_3_1.java        → boucle accept() + un thread par client
│   ├── ClientProcess_3_1.java   → thread qui traite un client (CC)
│   ├── ServerMT_3_2.java        → idem + compteur global synchronisé
│   └── ClientProcess_3_2.java   → thread qui traite un client (objets)
└── commonPackage/
    └── Operation.java           → objet Serializable partagé (repris du TP2-2)
```

## Activités

### Activité 3‑1 — Acceptation de plusieurs clients

Le serveur est transformé pour accepter **plusieurs connexions successives et simultanées** :

- Une **boucle `while (true)`** autour de `accept()` permet de servir un nombre illimité de clients sans redémarrer le serveur.
- Pour chaque client accepté, un **nouveau thread** (`ClientProcess_3_1`) est lancé. Il hérite de `Thread` et redéfinit `run()`.
- Le serveur affiche, pour chaque client : son **adresse IP** (via `socket.getRemoteSocketAddress()`) et son **numéro d'ordre** de connexion.
- Le client utilise `InetAddress` et `InetSocketAddress` pour se connecter, comme demandé.

### Activité 3‑2 — Calculatrice sur serveur multi-thread

Le service de calculatrice du TP2‑2 est intégré dans le serveur multi-thread :

- La communication se fait **par objets** : le client envoie une `Operation` (`Serializable`), le serveur la désérialise, calcule, puis renvoie le résultat sous forme de `String`.
- Un **compteur global** (`static int compteurOperations`) est partagé entre tous les threads. Il est incrémenté à chaque opération calculée avec succès.
- L'accès au compteur passe par une méthode **`synchronized static`**, ce qui évite les **conditions de course** (deux threads lisant et réécrivant la même valeur en même temps).

## Protocole d'échange

### Activité 3‑1 (CC)

| Étape | Sens             | Type        | Contenu                    |
|-------|------------------|-------------|----------------------------|
| 1     | Client → Serveur | ligne texte | premier opérande           |
| 2     | Client → Serveur | ligne texte | opérateur                  |
| 3     | Client → Serveur | ligne texte | deuxième opérande          |
| 4     | Serveur → Client | ligne texte | résultat ou `Erreur : ...` |

### Activité 3‑2 (objet)

| Étape | Sens             | Type            | Contenu                       |
|-------|------------------|-----------------|-------------------------------|
| 1     | Client → Serveur | objet sérialisé | `Operation(a, op, b)`         |
| 2     | Serveur → Client | objet sérialisé | `String` (résultat ou erreur) |

## Exécution

### En local (même machine)

1. Lancer le serveur :
   ```bash
   java serverPackage.ServerMT_3_2
   ```
2. Lancer plusieurs clients en parallèle (plusieurs terminaux) :
   ```bash
   java clientPackage.Client_3_2
   ```

### En réseau local (plusieurs machines)

1. Sur la machine serveur, récupérer son IP (`ipconfig` / `ifconfig` / `ip a`).
2. Dans `Client_3_2`, remplacer `"localhost"` par l'IP du serveur.
3. Lancer le serveur sur une machine, puis les clients sur les autres.

### Test avec Telnet (3‑1)

Sur Windows, activer le client Telnet via :
*Panneau de configuration → Programmes → Activer ou désactiver des fonctionnalités Windows → Client Telnet*

Puis :
```bash
telnet localhost 1234
```
Taper les trois lignes de l'opération (opérande, opérateur, opérande). Le serveur répond avec le résultat.

## Exemple

Console serveur :

```
Serveur multi-thread en écoute sur le port 1234
Client n°1 connecté, adresse : /192.168.1.20:54321
Client n°2 connecté, adresse : /192.168.1.21:54322
Client n°1 a envoyé : 55.0 * 12.0
>> Nombre total d'opérations traitées : 1
Client n°2 a envoyé : 7.0 / 0.0
Client n°3 connecté, adresse : /127.0.0.1:54330
Client n°3 a envoyé : 10.0 + 5.0
>> Nombre total d'opérations traitées : 2
```

Côté client :

```
Premier opérande : 55
Opérateur (+ - * /) : *
Deuxième opérande : 12
Réponse : 660.0
```

## Concepts abordés

- Serveur multi-thread via `Thread` et `run()`
- Boucle `accept()` pour accepter plusieurs clients
- Identification des clients (`getRemoteSocketAddress()`, numéro d'ordre)
- Sérialisation d'objets (`Serializable`, `ObjectInputStream`, `ObjectOutputStream`)
- Synchronisation d'une ressource critique partagée (`synchronized`, race conditions)
- Communication réseau en local et en réseau local
- Test d'un serveur TCP avec Telnet

## Environnement

- JDK ≥ 1.8 (nécessite Java 14+ pour les `switch` expressions utilisées dans `ClientProcess_3_1/3_2`)
- Eclipse / IntelliJ IDEA / VS Code
- Git

## Livrables

- Lien du repository GitHub
- Ce fichier `README.md`