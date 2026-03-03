-- 机构类型表
drop table if exists bus_doc_dept_type;
create table bus_doc_dept_type
(
    id          bigint auto_increment comment 'id' primary key,
    type_name   varchar(100) not null comment '类型名称',
    parent_id   bigint       not null default 0 comment '父id',
    ancestors   varchar(500) not null default '' comment '祖级列表',
    sort        int          not null default 0 comment '顺序',

    deleted     bigint       not null default 0 comment '逻辑删除',
    creator     bigint       not null default 0 comment '创建人',
    create_time datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    updater     bigint       not null default 0 comment '更新人',
    update_time datetime     not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '机构类型表';

-- 机构表
drop table if exists bus_doc_dept;
create table bus_doc_dept
(
    id               bigint auto_increment comment 'id' primary key,
    dept_name        varchar(200) not null comment '机构名称',
    official_name    varchar(100) not null comment '机构规范化简称',
    dept_type        bigint       not null default 100 comment '机构类型',
    dept_level       varchar(50)           default '' comment '机构级别(正副厅/本专科)',
    manage_dept_id   bigint                default null comment '主管机构ID(与 manage_dept_name 两者只有一个有值)',
    manage_dept_name varchar(200)          default '' comment '主管机构名称(与 manage_dept_id 两者只有一个有值)',
    sort             int          not null default 0 comment '顺序',

    deleted          bigint       not null default 0 comment '逻辑删除',
    creator          bigint       not null default 0 comment '创建人',
    create_time      datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    updater          bigint       not null default 0 comment '更新人',
    update_time      datetime     not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '机构表';

-- 职务表
drop table if exists bus_doc_post;
create table bus_doc_post
(
    id            bigint auto_increment comment 'id' primary key,
    post_name     varchar(200) not null comment '职务名称',
    official_name varchar(100) not null comment '职务正文名称',
    dept_id       bigint                default null comment '所属机构',

    deleted       bigint       not null default 0 comment '逻辑删除',
    creator       bigint       not null default 0 comment '创建人',
    create_time   datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    updater       bigint       not null default 0 comment '更新人',
    update_time   datetime     not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '职务表';






