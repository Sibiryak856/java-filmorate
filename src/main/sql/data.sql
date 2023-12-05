merge into PUBLIC.GENRES
using (select 'Комедия' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Драма' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Мультфильм' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Триллер' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Документальный' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Комедия' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);
merge into PUBLIC.GENRES
using (select 'Боевик' as GENRE_NAME) g
on    (PUBLIC.GENRES.GENRE_NAME = g.GENRE_NAME)
when  not matched then insert(GENRE_NAME) values(g.GENRE_NAME);

merge into PUBLIC.MPA
using (select 'G' as MPA_NAME) m
on    (PUBLIC.MPA.MPA_NAME = m.MPA_NAME)
when  not matched then insert(MPA_NAME) values(m.MPA_NAME);
merge into PUBLIC.GENRES
using (select 'PG' as MPA_NAME) m
on    (PUBLIC.MPA.MPA_NAME = m.MPA_NAME)
when  not matched then insert(MPA_NAME) values(m.MPA_NAME);
merge into PUBLIC.GENRES
using (select 'PG-13' as MPA_NAME) m
on    (PUBLIC.MPA.MPA_NAME = m.MPA_NAME)
when  not matched then insert(MPA_NAME) values(m.MPA_NAME);
merge into PUBLIC.GENRES
using (select 'R' as MPA_NAME) m
on    (PUBLIC.MPA.MPA_NAME = m.MPA_NAME)
when  not matched then insert(MPA_NAME) values(m.MPA_NAME);
merge into PUBLIC.GENRES
using (select 'NC-17' as MPA_NAME) m
on    (PUBLIC.MPA.MPA_NAME = m.MPA_NAME)
when  not matched then insert(MPA_NAME) values(m.MPA_NAME);


--INSERT INTO PUBLIC.GENRES (GENRE_NAME) VALUES('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');
--INSERT INTO PUBLIC.MPA  (MPA_NAME) VALUES('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');