DROP DATABASE IF EXISTS tbrpg;
CREATE DATABASE tbrpg;
USE tbrpg;


DROP TABLE IF EXISTS Player;
CREATE TABLE Player
(
id int PRIMARY KEY,
name varchar(30) NOT NULL
);



DROP TABLE IF EXISTS Stats;
CREATE TABLE Stats
(
id int PRIMARY KEY,
name varchar(30) NOT NULL,
baseHp int NOT NULL check (baseHp >= 0),
baseSp int NOT NULL check (baseSp >= 0),
atk int NOT NULL check (atk between 1 and 100),
mAtk int NOT NULL check (mAtk between 1 and 100),
def int NOT NULL check (def between 1 and 100),
mDef int NOT NULL check (mDef between 1 and 100),
crit int NOT NULL check (crit between 1 and 100),
eva int NOT NULL check (eva between 1 and 100),
skillList int NOT NULL check (skillList between 0 and 100),
unique (name)
);



DROP TABLE IF EXISTS Hero;
CREATE TABLE Hero
(
id int NOT NULL,
playerId int NOT NULL,
maxHp int NOT NULL check (maxHp between 1 and 4000),
currHp int NOT NULL,
maxSp int NOT NULL check (maxSp between 1 and 1000),
currSp int NOT NULL,
exp int NOT NULL check (exp between 0 and 4000),
lvl int NOT NULL check (lvl between 1 and 100), 
PRIMARY KEY (id, playerId),
FOREIGN KEY (id) references Stats (id) ON UPDATE CASCADE,
FOREIGN KEY (playerId) references Player (id) ON UPDATE CASCADE,
constraint currHp check (currHp <= maxHp),
constraint currSp check (currSp <= maxSp) 
);



DROP TABLE IF EXISTS Enemy;
CREATE TABLE Enemy
(
id int NOT NULL,
playerId int NOT NULL,
version char NOT NULL check (version in ('A', 'B', 'C', 'D', 'E', 'F')),
maxHp int NOT NULL check (maxHp between 1 and 4000),
currHp int NOT NULL,
expYield int NOT NULL check (expYield between 1 and 1000),
FOREIGN KEY (id) references Stats (id) ON UPDATE CASCADE,
FOREIGN KEY (playerId) references Player (id) ON UPDATE CASCADE,
PRIMARY KEY (id, playerId, version)
);



DROP TABLE IF EXISTS Skill;
CREATE TABLE Skill
(
id int NOT NULL,
name varchar(30) NOT NULL,
cost int NOT NULL,
atk int NOT NULL,
mAtk int NOT NULL,
crit int NOT NULL,
exp int NOT NULL,
description varchar(100) NOT NULL,
unique (name)
);



DROP TABLE IF EXISTS Item;
CREATE TABLE Item
(
id int PRIMARY KEY,
name varchar(30) NOT NULL,
hpRec int NOT NULL,
spRec int NOT NULL,
price int,
description varchar(100) NOT NULL,
unique (name)
);



DROP TABLE IF EXISTS ItemQuantity;
CREATE TABLE ItemQuantity
(
id int NOT NULL,
playerId int NOT NULL,
quantity int,
PRIMARY KEY (id, playerId),
FOREIGN KEY (id) references Item (id) ON UPDATE CASCADE,
FOREIGN KEY (playerID) references Player (id) ON UPDATE CASCADE
);