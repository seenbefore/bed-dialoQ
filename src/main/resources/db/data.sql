-- 插入测试用户数据
INSERT INTO users (username, password, real_name, email, phone, avatar, last_login_time, created_at) 
VALUES 
('test1', 'password1', '测试用户1', 'test1@example.com', '13800138001', '/avatar/default.png', NOW(), NOW()),
('test2', 'password2', '测试用户2', 'test2@example.com', '13800138002', '/avatar/default.png', NOW(), NOW()),
('zhangsan', 'password3', '张三', 'zhangsan@example.com', '13800138003', '/avatar/default.png', NOW(), NOW()),
('lisi', 'password4', '李四', 'lisi@example.com', '13800138004', '/avatar/default.png', NOW(), NOW()); 