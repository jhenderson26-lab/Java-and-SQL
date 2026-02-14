INSERT INTO Person (first_name, last_name) VALUES
-- ('John', 'Smith'),
('Emma', 'Johnson'),
('Michael', 'Brown'),
('Sophia', 'Williams'),
('Daniel', 'Miller'),
('Olivia', 'Davis'),
('James', 'Wilson'),
('Isabella', 'Taylor');

INSERT INTO Books (title, author, publish_year, copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 1925, 5),
('To Kill a Mockingbird', 'Harper Lee', 1960, 4),
('1984', 'George Orwell', 1949, 6),
('Pride and Prejudice', 'Jane Austen', 1813, 3),
('The Hobbit', 'J.R.R. Tolkien', 1937, 7),
('The Catcher in the Rye', 'J.D. Salinger', 1951, 2),
('Moby-Dick', 'Herman Melville', 1851, 3),
('The Alchemist', 'Paulo Coelho', 1988, 5);

INSERT INTO BookHistory (person_id, book_id, due_date, return_date) VALUES
-- (1, 3, CURRENT_DATE + INTERVAL '14 days', NULL),
(2, 1, CURRENT_DATE + INTERVAL '7 days', NULL),
(3, 5, CURRENT_DATE + INTERVAL '10 days', CURRENT_DATE - INTERVAL '2 days'),
(4, 2, CURRENT_DATE + INTERVAL '14 days', NULL),
(5, 4, CURRENT_DATE + INTERVAL '21 days', CURRENT_DATE - INTERVAL '5 days');

