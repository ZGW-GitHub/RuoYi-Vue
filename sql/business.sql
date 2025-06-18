-- 新增字典：性别
delete from sys_dict_type where dict_type = 'bus_common_gender';
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('性别', 'bus_common_gender', '0', 'admin', NOW(), '');
delete from sys_dict_data where dict_type = 'bus_common_gender';
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES (1, '保密', '0', 'bus_common_gender', '', '', 'N', '0', 'admin', NOW(), ''),
       (2, '男', '1', 'bus_common_gender', '', '', 'N', '0', 'admin', NOW(), ''),
       (3, '女', '2', 'bus_common_gender', '', '', 'N', '0', 'admin', NOW(), '');
