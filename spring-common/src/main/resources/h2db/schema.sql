CREATE TABLE t_user (
  id varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  sex varchar(2) DEFAULT NULL,
  age int(11) DEFAULT NULL,
  card_no varchar(100) NOT NULL,
  PRIMARY KEY (id)
);