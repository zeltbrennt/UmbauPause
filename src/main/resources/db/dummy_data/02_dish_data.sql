insert into shop.dish (created_at, updated_at, description)
values (now(), now(), 'cremige Polenta mit gebratenen Champignons und Tomaten-Mangold'),
       (now(), now(), 'Kartoffeln mit Senfsoße und Ei'),
       (now(), now(), 'gegrillter Spitzkohl mit Süßkartoffelstampf und Nam Pik (thailändische Tomatensalsa)'),
       (now(), now(), 'Tikka Masala Curry mit Kürbis, Bohnen dazu Basmatireis und Korianderjoghurt'),
       (now(), now(), 'Pasta Genovese - Karotten, Staudensellerie und Parmesan'),
       (now(), now(), 'Rotes Linsendal mit Spinat, Schwarzkümmel und Blumenkohl'),
       (now(), now(), 'geschmorter Tomatentofu mit Ingwer, Zitronengras und Reis - vegan'),
       (now(), now(), 'Grünes Gemüsecurry mit Nüssen und Kräutern - dazu Fladenbrot'),
       (now(), now(), 'Knuspriger Räuchertofu, Gurkensalat und Kartoffelbrei'),
       (now(), now(), 'Gemüsetajine mit Aubergine und Salzzitrone, dazu Fladenbrot'),
       (now(), now(), 'Gelbes Kokoscurry mit Cashewkernen, Kräutern und Basmatireis'),
       (now(), now(),
        'Bibimbap - Koreanische Lunchbowl mit Karotte, Gurke, hausgemachtem Kimchi, Hack, Sprossen und Solei'),
       (now(), now(), 'Pasta con crema di parmigiano');

insert into shop.menu (created_at, updated_at, valid_from, valid_to, day_of_week, dish_id)
values (now(), now(), date('2024-07-29'), date('2024-08-02'), 1, 1),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 2, 2),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 3, 3),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 4, 4),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 5, 5),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 1, 6),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 2, 7),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 3, 8),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 4, 9),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 5, 10);

