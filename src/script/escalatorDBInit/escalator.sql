/* sql 콘솔 열고 */
create database roadbuddy;

/* roadbuddy 콘솔 열고 */
create schema rb;

/* rb 스키마 콘솔 열고 */
create table escalator (
    escalator_id integer primary key,
    escalator_line integer,
    escalator_name varchar,
    escalator_kth integer,
    escalator_number varchar,
    escalator_pos varchar,
    escalator_section varchar,
    escalator_section_direction varchar
)
