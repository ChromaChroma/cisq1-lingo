create table feedback
(
    id      uuid not null,
    attempt varchar(255),
    primary key (id)
);

create table feedback_marks
(
    feedback_id uuid not null,
    marks       varchar(255)
);

create table game
(
    id          uuid not null,
    game_state  varchar(255),
    word_length varchar(255),
    score_id    uuid,
    primary key (id)
);

create table game_rounds
(
    game_id   uuid not null,
    rounds_id uuid not null
);

create table round
(
    id    uuid not null,
    state varchar(255),
    word  varchar(255),
    primary key (id)
);

create table round_turn_mapping
(
    round_id  uuid not null,
    turn_id   uuid not null,
    turns_key int4 not null,
    primary key (round_id, turns_key)
);

create table score
(
    id            uuid not null,
    points        int4,
    rounds_played int4,
    primary key (id)
);

create table turn
(
    id          uuid not null,
    feedback_id uuid,
    primary key (id)
);

create table words
(
    word   varchar(255) not null,
    length int4,
    primary key (word)
);

alter table if exists game_rounds
    add constraint UK_gwn3wpw21acccak4odbxhpola unique (rounds_id);

alter table if exists round_turn_mapping
    add constraint UK_jx12602ytr6fcbvp12g2kid4l unique (turn_id);

alter table if exists feedback_marks
    add constraint FKaed0swgv9a4yswuye8xj7718q
        foreign key (feedback_id)
            references feedback;

alter table if exists game
    add constraint FKng1j875nyvy2ajwns328t9ddu
        foreign key (score_id)
            references score;

alter table if exists game_rounds
    add constraint FK378y0jg9i07kroelcx5qif7ww
        foreign key (rounds_id)
            references round;

alter table if exists game_rounds
    add constraint FKhwg9acgvm267aqpi5wj3wm54h
        foreign key (game_id)
            references game;

alter table if exists round_turn_mapping
    add constraint FK8k6s8ccg5j3sqvf2xi639mnka
        foreign key (turn_id)
            references turn;

alter table if exists round_turn_mapping
    add constraint FKs5x1lpsf1j2bc0dlkak4k6pbd
        foreign key (round_id)
            references round;

alter table if exists turn
    add constraint FKfcanpvnw1309lj41h68u2lx7b
        foreign key (feedback_id)
            references feedback;

create table feedback
(
    id      uuid not null,
    attempt varchar(255),
    primary key (id)
);

create table feedback_marks
(
    feedback_id uuid not null,
    marks       varchar(255)
);

create table game
(
    id          uuid not null,
    game_state  varchar(255),
    word_length varchar(255),
    score_id    uuid,
    primary key (id)
);

create table game_rounds
(
    game_id   uuid not null,
    rounds_id uuid not null
);

create table round
(
    id    uuid not null,
    state varchar(255),
    word  varchar(255),
    primary key (id)
);

create table round_turn_mapping
(
    round_id  uuid not null,
    turn_id   uuid not null,
    turns_key int4 not null,
    primary key (round_id, turns_key)
);

create table score
(
    id            uuid not null,
    points        int4,
    rounds_played int4,
    primary key (id)
);

create table turn
(
    id          uuid not null,
    feedback_id uuid,
    primary key (id)
);

create table words
(
    word   varchar(255) not null,
    length int4,
    primary key (word)
);

alter table if exists game_rounds
    add constraint UK_gwn3wpw21acccak4odbxhpola unique (rounds_id);

alter table if exists round_turn_mapping
    add constraint UK_jx12602ytr6fcbvp12g2kid4l unique (turn_id);

alter table if exists feedback_marks
    add constraint FKaed0swgv9a4yswuye8xj7718q
        foreign key (feedback_id)
            references feedback;

alter table if exists game
    add constraint FKng1j875nyvy2ajwns328t9ddu
        foreign key (score_id)
            references score;

alter table if exists game_rounds
    add constraint FK378y0jg9i07kroelcx5qif7ww
        foreign key (rounds_id)
            references round;

alter table if exists game_rounds
    add constraint FKhwg9acgvm267aqpi5wj3wm54h
        foreign key (game_id)
            references game;

alter table if exists round_turn_mapping
    add constraint FK8k6s8ccg5j3sqvf2xi639mnka
        foreign key (turn_id)
            references turn;

alter table if exists round_turn_mapping
    add constraint FKs5x1lpsf1j2bc0dlkak4k6pbd
        foreign key (round_id)
            references round;

alter table if exists turn
    add constraint FKfcanpvnw1309lj41h68u2lx7b
        foreign key (feedback_id)
            references feedback;
