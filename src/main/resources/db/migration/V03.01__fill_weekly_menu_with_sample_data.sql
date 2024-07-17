insert into shop.weekly_menu (monday, tuesday, wednesday, thursday, friday, valid_from, valid_to)
values ('Knuspriger Räuchertofu, Gurkensalat und Kartoffelbrei',
        'Gemüsetajine mit Aubergine und Salzzitrone, dazu Fladenbrot',
        'Gelbes Kokoscurry mit Cashewkernen, Kräutern und Basmatireis',
        'Bibimbap - Koreanische Lunchbowl mit Karotte, Gurke, hausgemachtem Kimchi, Hack, Sprossen und Solei',
        'Pasta con crema di parmigiano',
        make_date(2024, 1, 1),
        make_date(2025, 12, 31));