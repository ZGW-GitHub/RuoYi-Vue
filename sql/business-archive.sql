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

create table bus_cadre_archive_item
(
    id           bigint comment 'id' primary key,
    archive_id   bigint      not null comment '干部档案id',
    archive_type varchar(50) not null comment '档案类别',
    item_name    varchar(30)          default '' null comment '部门名称',
    order_num    int                  default 0 null comment '顺序',
    parent_id    bigint               default 0 null comment '父id',
    ancestors    varchar(50)          default '' null comment '祖级列表',

    deleted      bigint      not null default 0 comment '逻辑删除',
    creator      bigint      not null default 0 comment '创建人',
    create_time  datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updater      bigint      not null default 0 comment '更新人',
    update_time  datetime    not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '档案项表';



-- 新增字典：档案在库状态
delete from sys_dict_type where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('档案在库状态', 'bus_archive_stock_status', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '在库', '1', 'bus_archive_stock_status', '', '', 'Y', '0', 'admin', NOW(), ''),
       (2, '离库', '0', 'bus_archive_stock_status', '', '', 'N', '0', 'admin', NOW(), '');


