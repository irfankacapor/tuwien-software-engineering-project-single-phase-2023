CREATE TABLE IF NOT EXISTS breed
(
  id BIGINT PRIMARY KEY,
  name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS horse
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  -- Instead of an ENUM (H2 specific) this could also be done with a character string type and a check constraint.
  sex ENUM ('MALE', 'FEMALE') NOT NULL,
  date_of_birth DATE NOT NULL,
  height NUMERIC(4,2),
  weight NUMERIC(7,2),
  breed_id BIGINT REFERENCES breed(id)
);

CREATE TABLE IF NOT EXISTS tournament (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  start_date DATE,
  end_date DATE
);

CREATE TABLE IF NOT EXISTS participation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tournament_id INT,
  horse_id INT,
  FOREIGN KEY (tournament_id) REFERENCES tournament(id),
  FOREIGN KEY (horse_id) REFERENCES horse(id)
);
