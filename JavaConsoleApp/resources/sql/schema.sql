begin;

create table users (
    id integer primary key ,
    login varchar(20) unique
);


create table projects
(
    id   integer primary key,
    name varchar(20) unique
);


create table tasks (
    id integer primary key,
    type integer,
    topic varchar unique,
    priority integer,
    project_id integer,
    user_id integer,
    description varchar(150),
    foreign key(user_id) references users(id),
    foreign key(project_id) references projects(id)
);

PRAGMA foreign_keys = ON;

commit;
