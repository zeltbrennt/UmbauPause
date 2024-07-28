DO
$$
    DECLARE
        start_date DATE := '2023-12-31';
        kw         int;
        year       int ;
        quarter    int ;
        end_date   DATE;

    BEGIN
        FOR i IN 1..100
            LOOP
                end_date := start_date + INTERVAL '6 days';
                kw := date_part('week', end_date);
                year := date_part('year', end_date);
                quarter := date_part('quarter', end_date);
                INSERT INTO shop.week (week_start, week_end, kw, year, quarter)
                VALUES (start_date, end_date, kw, year, quarter);
                start_date := start_date + INTERVAL '7 days';
            END LOOP;
    END
$$;


insert into shop.day (id, name)
values (1, 'Montag'),
       (2, 'Dienstag'),
       (3, 'Mittwoch'),
       (4, 'Donnerstag'),
       (5, 'Freitag'),
       (6, 'Saturday'),
       (7, 'Sunday');