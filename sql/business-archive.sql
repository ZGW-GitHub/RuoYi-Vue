drop table if exists bus_cadre_archive;
create table bus_cadre_archive
(
    id                   bigint auto_increment comment 'id' primary key,
    cadre_name           varchar(100) not null comment '干部姓名',
    cadre_name_py        varchar(100) not null comment '干部姓名拼音首字母',
    gender               varchar(2)   not null default '0' comment '性别',
    id_number            varchar(32)  not null default '' comment '身份证号',
    birthday             varchar(30)  not null default '' comment '出生年月',
    ethnic               varchar(10)  not null default '' comment '民族',
    dept_id              bigint       not null default 100 comment '部门ID',
    archive_file_path    varchar(255) not null default '' comment '档案存放路径',
    archive_stock_status varchar(20)  not null default '' comment '档案在库状态',

    deleted              bigint       not null default 0 comment '逻辑删除',
    creator              bigint       not null default 0 comment '创建人',
    create_time          datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    updater              bigint       not null default 0 comment '更新人',
    update_time          datetime     not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '干部档案表';

drop table if exists bus_cadre_archive_item;
create table bus_cadre_archive_item
(
    id                  bigint auto_increment comment 'id' primary key,
    archive_id          bigint                 not null comment '干部档案id',
    item_type           varchar(50)            not null comment '档案类别',
    item_name           varchar(30) default '' not null comment '档案名称',
    material_date       varchar(30) default '' not null comment '材料日期',
    material_page_count int         default 0  not null comment '材料页数',
    parent_id           bigint      default 0  not null comment '父id',
    ancestors           varchar(200) default '' not null comment '祖级列表',
    sort                int         default 0  not null comment '顺序',

    deleted             bigint                 not null default 0 comment '逻辑删除',
    creator             bigint                 not null default 0 comment '创建人',
    create_time         datetime               not null default CURRENT_TIMESTAMP comment '创建时间',
    updater             bigint                 not null default 0 comment '更新人',
    update_time         datetime               not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '档案项表';



