/*create table rabbit (
id serial primary key,
   created_data varchar(2000)
);*/

create table if not exists posts(
id serial primary key,
name varchar(300),
link varchar(300),
text text,
createData varchar(255)
);