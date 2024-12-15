use tbrpg;

insert into Player values (0, 'Base');
insert into Player values (2, 'Human Person');
insert into Player values (3, 'Pyrson');
insert into Player values (5, 'User5');
insert into Player values (7, 'Yuu Nohem');
insert into Player values (80, 'Strong');

insert into Stats values (1, 'Hiiro', 100, 50, 10, 2, 15, 10, 15, 5, 30);
insert into Stats values (2, 'Tim', 70, 70, 10, 10, 5, 5, 20, 20, 30);
insert into Stats values (3, 'Tank', 100, 20, 5, 5, 50, 50, 10, 2, 20);
insert into Stats values (4, 'Homura', 30, 7, 1, 13, 7, 11, 10, 15, 34);
insert into Stats values (5, 'Op', 100, 100, 100, 100, 100, 100, 50, 50, 1);
insert into Stats values (33, 'Gremlin', 100, 500, 20, 20, 20, 20, 5, 5, 2);
insert into Stats values (40, 'Bossa', 1000, 500, 1, 30, 10, 10, 10, 3, 4);
insert into Stats values (50, 'Nova', 2000, 5000, 20, 20, 100, 100, 30, 1, 7);

insert into Hero values (1, 2, 158, 158, 70, 70, 340, 10);
insert into Hero values (1, 3, 158, 158, 70, 63, 302, 10);
insert into Hero values (1, 5, 158, 103, 70, 30, 357, 10);
insert into Hero values (1, 7, 158, 0, 70, 5, 399, 10);
insert into Hero values (1, 80, 158, 28, 70, 70, 363, 10);

insert into Enemy values (33, 0, 'A', 100, 100, 20);
insert into Enemy values (33, 5, 'A', 100, 100, 20);
insert into Enemy values (33, 5, 'B', 100, 53, 20);
insert into Enemy values (40, 0, 'A', 1000, 1000, 100);
insert into Enemy values (40, 7, 'A', 1000, 400, 100);
insert into Enemy values (50, 0, 'A', 3000, 3000, 1000);

insert into Skill values (1, 'Attack', 0, 1, 0, 10, 0);
insert into Skill values (6, 'Heal', 15, 0, -30, 0, 100);
insert into Skill values (12, 'Healga', 40, 0, -60, 0, 700);
insert into Skill values (20, 'Treat', -5, 0, -10, 0, 300);
insert into Skill values (22, 'Slash', 20, 30, 0, 15, 300);

insert into Item values (1, 'Potion', 100, 0, 10, 'A basic potion. Heals 100 HP.');
insert into Item values (4, 'SP Potion', 0, 50, 100, 'A special potion. Heals 50 SP.');
insert into Item values (99, 'Old Key', 0, 0, NULL, 'An odd item that opens something.');

insert into ItemQuantity values (1, 2, 10);
insert into ItemQuantity values (1, 5, 10);
insert into ItemQuantity values (4, 80, 3);
insert into ItemQuantity values (99, 2, 1);
 