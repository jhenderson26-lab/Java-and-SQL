
CREATE TABLE Person (
    person_id SERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL
);

CREATE TABLE Books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    author VARCHAR(50) NOT NULL,
    publish_year INTEGER CHECK ( publish_year BETWEEN 0 AND 9999),
    copies INTEGER NOT NULL CHECK ( copies >= 0 )
);

CREATE TABLE BookHistory (
    history_id SERIAL PRIMARY KEY,
    person_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    checkout_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (person_id) REFERENCES Person(person_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

