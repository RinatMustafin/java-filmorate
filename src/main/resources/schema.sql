CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id
    INTEGER
    PRIMARY
    KEY,
    description
    VARCHAR
);

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
),
    login VARCHAR
(
    10
) NOT NULL CHECK
(
    LENGTH
(
    login
) >= 5),
    email VARCHAR
(
    255
) NOT NULL,
    birthday DATE
    );

CREATE TABLE IF NOT EXISTS film
(
    film_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
    NOT
    NULL,
    description
    VARCHAR
(
    200
),
    release_date DATE,
    duration INTEGER CHECK
(
    duration >
    0
),
    mpa_id INTEGER REFERENCES mpa
(
    mpa_id
)
    );

CREATE TABLE IF NOT EXISTS genre
(
    genre_id
    INTEGER
    PRIMARY
    KEY,
    description
    VARCHAR
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id
    INTEGER
    REFERENCES
    film
(
    film_id
) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genre
(
    genre_id
)
  ON DELETE CASCADE,
    PRIMARY KEY
(
    film_id,
    genre_id
)
    );

CREATE TABLE IF NOT EXISTS likes
(
    film_id
    INTEGER
    REFERENCES
    film
(
    film_id
) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    PRIMARY KEY
(
    film_id,
    user_id
)
    );

CREATE TABLE IF NOT EXISTS friendship
(
    user_id
    BIGINT
    REFERENCES
    users
(
    id
) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    PRIMARY KEY
(
    user_id,
    friend_id
)
    );