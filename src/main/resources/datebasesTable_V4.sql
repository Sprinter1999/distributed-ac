DROP TABLE IF EXISTS  User;
DROP TABLE IF EXISTS  Bill;
DROP TABLE IF EXISTS  Record;
DROP TABLE IF EXISTS  Conditioner;

CREATE TABLE User(
  user_id INT AUTO_INCREMENT ,
  PASSWORD NVARCHAR(20),
  room_id INT,
  checkin BIGINT,
  checkout BIGINT,
  PRIMARY KEY (user_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Bill(
  transaction_id INT AUTO_INCREMENT ,
  room_id INT,
  user_id INT,
  checkin BIGINT,
  checkout BIGINT,
  total_fee DOUBLE(10,2),
  PRIMARY KEY (transaction_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Record(
    record_id INT AUTO_INCREMENT,
    user_id INT,
    room_id INT,
    wind_speed INT,
    start_temp DOUBLE,
    set_temp DOUBLE,
    request_start_time BIGINT,
    actual_start_time BIGINT,
    end_time BIGINT,
    electricity DOUBLE DEFAULT 0,
    is_complete INT DEFAULT 0,
    PRIMARY KEY (record_id)
)DEFAULT CHARSET=utf8;

CREATE TABLE Conditioner(
    room_id INT,
    user_id INT,
    init_temp DOUBLE DEFAULT 25,
    set_temp DOUBLE DEFAULT 25,
    cur_temp DOUBLE DEFAULT 25,
    is_at_work INT DEFAULT 0,
    serve_start_time BIGINT DEFAULT 0,
    wait_start_time BIGINT DEFAULT 0,
    wind_speed INT DEFAULT 0,
    PRIMARY KEY(room_id)
)DEFAULT CHARSET=utf8;

INSERT INTO Conditioner VALUE (1, 1, 26, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (2, 2, 27, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (3, 3, 26, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (4, 4, 25, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (5, 5, 26, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (6, 6, 28, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (7, 7, 27, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (8, 8, 25, 25, 25, 0, 0, 0, 0);
INSERT INTO Conditioner VALUE (9, 9, 25, 25, 25, 0, 0, 0, 0);