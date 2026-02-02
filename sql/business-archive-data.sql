-- 新增字典：档案在库状态
delete from sys_dict_type where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('档案在库状态', 'bus_archive_stock_status', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_archive_stock_status';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '在库', '1', 'bus_archive_stock_status', '', '', 'Y', '0', 'admin', NOW(), ''),
       (2, '离库', '0', 'bus_archive_stock_status', '', '', 'N', '0', 'admin', NOW(), '');



