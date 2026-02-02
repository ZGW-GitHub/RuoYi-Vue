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
    id           bigint comment 'id' primary key,
    archive_id   bigint      not null comment '干部档案id',
    archive_type varchar(50) not null comment '档案类别',
    item_name    varchar(30)          default '' null comment '部门名称',
    parent_id    bigint               default 0 null comment '父id',
    ancestors    varchar(50)          default '' null comment '祖级列表',
    sort         int                  default 0 null comment '顺序',

    deleted      bigint      not null default 0 comment '逻辑删除',
    creator      bigint      not null default 0 comment '创建人',
    create_time  datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    updater      bigint      not null default 0 comment '更新人',
    update_time  datetime    not null default '1000-01-01 00:00:00' comment '更新时间'
) comment '档案项表';

INSERT INTO `bus_cadre_archive_item` (`id`, `archive_id`, `archive_type`, `item_name`, `parent_id`, `ancestors`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`)
VALUES
    (101, 1, '一', '履历材料', 0, '0', 1, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (102, 1, '二', '自传材料', 0, '0', 2, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (103, 1, '三', '鉴定、考核、考察材料', 0, '0', 3, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (104, 1, '四', '学历学位、职称、学术、培训等材料', 0, '0', 4, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (201, 1, '4-1', '学历学位材料', 0, '0', 5, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (202, 1, '4-2', '职业(任职)资格和评(聘)专业技术职务(职称)材料', 0, '0', 6, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (203, 1, '4-3', '科研学术材料', 0, '0', 7, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (204, 1, '4-4', '培训材料', 0, '0', 8, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (105, 1, '五', '政审材料', 0, '0', 9, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (106, 1, '六', '党团材料', 0, '0', 10, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (107, 1, '七', '奖励材料', 0, '0', 11, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (108, 1, '八', '处分材料', 0, '0', 12, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (109, 1, '九', '工资、任免、出国、会议等材料', 0, '0', 13, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (110, 1, '十', '其他材料', 0, '0', 14, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (211, 1, '9-1', '工资材料', 0, '0', 15, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (212, 1, '9-2', '任免材料', 0, '0', 16, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (213, 1, '9-3', '出国(境)材料', 0, '0', 17, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00'),
    (214, 1, '9-4', '参加会议的代表登记表等材料', 0, '0', 18, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');

# INSERT INTO bus_cadre_archive_item (id, archive_id, archive_type, item_name, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time)
# VALUES
#     (100, 1, '部门', '若依科技', 0, '0', 0, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (101, 1, '部门', '深圳总公司', 100, '0,100', 1, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (102, 1, '部门', '长沙分公司', 100, '0,100', 2, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (103, 1, '部门', '研发部门', 101, '0,100,101', 1, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (104, 1, '部门', '市场部门', 101, '0,100,101', 2, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (105, 1, '部门', '测试部门', 101, '0,100,101', 3, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (106, 1, '部门', '财务部门', 101, '0,100,101', 4, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (107, 1, '部门', '运维部门', 101, '0,100,101', 5, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (108, 1, '部门', '市场部门', 102, '0,100,102', 1, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00'),
#     (109, 1, '部门', '财务部门', 102, '0,100,102', 2, 0, 1, '2025-09-13 21:05:10', 0, '1000-01-01 00:00:00');




-- 新增字典：档案在库状态
delete from sys_dict_type where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('档案在库状态', 'bus_archive_stock_status', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '在库', '1', 'bus_archive_stock_status', '', '', 'Y', '0', 'admin', NOW(), ''),
       (2, '离库', '0', 'bus_archive_stock_status', '', '', 'N', '0', 'admin', NOW(), '');


