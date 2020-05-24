create table t_user
(
    id      varchar(10)            not null comment '工号' primary key,
    name    varchar(50)            not null comment '姓名',
    sex     varchar(2) default '0' not null comment '性别。0：male；1：female',
    age     int                    null comment '年龄',
    card_no varchar(30)            not null comment '身份证号'
) comment '用户表';

create table t_quartz_job
(
    name        varchar(50)                        not null primary key,
    `group`     varchar(50)                        null comment '定时任务分组',
    class_name  varchar(100)                       not null comment '任务类名，一般和任务名相同',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '定时任务表';

create table t_quartz_trigger
(
    name        varchar(50)                        not null comment '触发器名字' primary key,
    `group`     varchar(50)                        null comment '触发器分组',
    job_name    varchar(50)                        null comment '绑定的定时任务名字',
    status      varchar(10)                        null comment '触发器状态',
    start_time  varchar(50)                        null comment '开始时间',
    pre_time    varchar(50)                        null comment '上次执行时间',
    next_time   varchar(50)                        null comment '下次执行时间',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) comment '触发器表';

create table t_quartz_cron
(
    trigger_group varchar(50)                        not null comment '触发器分组' primary key,
    exp           varchar(50)                        not null comment '表达式',
    zone          varchar(10)                        null comment '时区',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '触发事件表达式';






