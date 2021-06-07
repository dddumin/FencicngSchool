use fencingschool;
create table User
(
    id       integer primary key auto_increment,
    login    varchar(50) not null unique,
    password varchar(50) not null,
    name     varchar(50) not null,
    date     date        not null
);


create table Apprentice
(
    id           integer primary key auto_increment,
    surname      varchar(50) not null,
    name         varchar(50) not null,
    patronymic   varchar(50),
    phone_number varchar(50) not null unique
);

create table Trainer
(
    id         integer primary key auto_increment,
    surname    varchar(50) not null,
    name       varchar(50) not null,
    patronymic varchar(50),
    experience integer     not null,
    unique (surname, name, experience)
);

create TABLE TrainerSchedule
(
    id               integer primary key,
    monday_start     time,
    monday_finish    time,
    tuesday_start    time,
    tuesday_finish   time,
    wednesday_start  time,
    wednesday_finish time,
    thursday_start   time,
    thursday_finish  time,
    friday_start     time,
    friday_finish    time,
    saturday_start   time,
    saturday_finish  time,
    sunday_start     time,
    sunday_finish    time,
    FOREIGN KEY (id) references Trainer (id) on delete cascade on update cascade
);

create table Training
(
    id            integer primary key auto_increment,
    number_gym    integer  not null,
    id_trainer    integer  not null,
    id_apprentice integer  not null,
    date          datetime not null,
    FOREIGN KEY (id_apprentice) references Apprentice (id) ON delete cascade on update cascade,
    FOREIGN KEY (id_trainer) references Trainer (id) on delete cascade on update cascade
);