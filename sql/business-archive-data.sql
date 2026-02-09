-- 新增字典：档案在库状态
delete from sys_dict_type where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('档案在库状态', 'bus_archive_stock_status', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '在库', '1', 'bus_archive_stock_status', '', '', 'Y', '0', 'admin', NOW(), ''),
       (2, '离库', '0', 'bus_archive_stock_status', '', '', 'N', '0', 'admin', NOW(), '');

-- 新增字典：档案项类型
delete from sys_dict_type where dict_type = 'bus_archive_item_type';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('档案项类型', 'bus_archive_item_type', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_archive_item_type';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '一、履历材料', '一', 'bus_archive_item_type', '', '', 'Y', '0', 'admin', NOW(), ''),
       (2, '二、自传材料', '二', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (3, '三、鉴定、考核、考察材料', '三', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (5, '4-1、学历学位材料', '4-1', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (6, '4-2、专业技术职务材料', '4-2', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (7, '4-3、科研学术材料', '4-3', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (8, '4-4、培训材料', '4-4', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (9, '五、政审材料', '五', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (10, '六、党团材料', '六', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (11, '七、奖励材料', '七', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (12, '八、处分材料', '八', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (14, '9-1、工资材料', '9-1', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (15, '9-2、任免材料', '9-2', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (16, '9-3、出国(境)材料', '9-3', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (17, '9-4、参加会议的代表登记表等材料', '9-4', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), ''),
       (18, '十、其他材料', '十', 'bus_archive_item_type', '', '', 'N', '0', 'admin', NOW(), '') ;


-- 4-2、职业(任职)资格和评(聘)专业技术职务(职称)材料
delete from bus_cadre_archive_item where archive_id = 0;
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (110, 0, '一', '一、履历材料', '', 0, 0, '0', 1, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (120, 0, '二', '二、自传材料', '', 0, 0, '0', 2, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (130, 0, '三', '三、鉴定、考核、考察材料', '', 0, 0, '0', 3, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (140, 0, '四', '四、学历学位、职称、学术、培训等材料', '', 0, 0, '0', 4, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (141, 0, '4-1', '4-1、学历学位材料', '', 0, 0, '0', 5, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (142, 0, '4-2', '4-2、专业技术职务材料', '', 0, 0, '0', 6, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (143, 0, '4-3', '4-3、科研学术材料', '', 0, 0, '0', 7, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (144, 0, '4-4', '4-4、培训材料', '', 0, 0, '0', 8, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (150, 0, '五', '五、政审材料', '', 0, 0, '0', 9, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (160, 0, '六', '六、党团材料', '', 0, 0, '0', 10, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (170, 0, '七', '七、奖励材料', '', 0, 0, '0', 11, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (180, 0, '八', '八、处分材料', '', 0, 0, '0', 12, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (190, 0, '九', '九、工资、任免、出国、会议等材料', '', 0, 0, '0', 13, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (191, 0, '9-1', '9-1、工资材料', '', 0, 0, '0', 14, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (192, 0, '9-2', '9-2、任免材料', '', 0, 0, '0', 15, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (193, 0, '9-3', '9-3、出国(境)材料', '', 0, 0, '0', 16, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (194, 0, '9-4', '9-4、参加会议的代表登记表等材料', '', 0, 0, '0', 17, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
INSERT INTO bus_cadre_archive_item (id, archive_id, item_type, item_name, material_date, material_page_count, parent_id, ancestors, sort, deleted, creator, create_time, updater, update_time) VALUES (200, 0, '十', '十、其他材料', '', 0, 0, '0', 18, 0, 0, '2025-05-08 14:21:30', 0, '1000-01-01 00:00:00');
