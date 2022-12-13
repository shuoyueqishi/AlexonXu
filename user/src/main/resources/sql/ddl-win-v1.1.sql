DROP TABLE IF EXISTS `user_t`;
CREATE TABLE `user_t`
(
    `user_id`          bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'user_id主键',
    `nick_name`        varchar(64)  DEFAULT NULL COMMENT '昵称',
    `name`             varchar(64)  DEFAULT NULL COMMENT '姓名',
    `password`         varchar(128) DEFAULT NULL COMMENT '密码',
    `telephone`        varchar(32)  DEFAULT NULL COMMENT '电话',
    `email`            varchar(128) DEFAULT NULL COMMENT '邮箱',
    `head_img`         varchar(20)  DEFAULT NULL COMMENT '头像，存储图片的docNo',
    `default_role`     bigint(10)   DEFAULT NULL COMMENT '默认角色',
    `create_by`        varchar(50)  DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   varchar(50)  DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`user_id`),
    KEY `idx_name` (`name`),
    KEY `idx_telephone` (`telephone`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10005
  DEFAULT CHARSET = utf8 COMMENT ='用户表';

DROP TABLE IF EXISTS `role_t`;
CREATE TABLE `role_t`
(
    `role_id`          bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
    `role_code`        varchar(64)  DEFAULT NULL COMMENT '角色编码',
    `role_name`        varchar(128) DEFAULT NULL COMMENT '角色名称',
    `role_desc`        varchar(256) DEFAULT NULL COMMENT '角色描述',
    `tenant`           varchar(32)  DEFAULT NULL COMMENT '租户',
    `create_by`        varchar(50)  DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   varchar(50)  DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `uk_nameTenant` (`role_code`, `tenant`),
    KEY `idx_roleName` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='角色表';

DROP TABLE IF EXISTS `operation_log_t`;
CREATE TABLE `operation_log_t`
(
    `id`             bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `operate_module` varchar(64)  DEFAULT NULL COMMENT '操作模块',
    `operate_type`   varchar(32)  DEFAULT NULL COMMENT '操作类型',
    `operate_desc`   varchar(128) DEFAULT NULL COMMENT '操作描述',
    `url`            varchar(128) DEFAULT NULL COMMENT '操作url',
    `request`        longtext COMMENT '请求参数',
    `response`       longtext COMMENT '返回参数',
    `user_ip`        varchar(64)  DEFAULT NULL COMMENT '操作人ip',
    `user_name`      varchar(64)  DEFAULT NULL COMMENT '操作人',
    `creation_time`  datetime     DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_name` (`user_name`),
    KEY `idx_operate_module` (`operate_module`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 413
  DEFAULT CHARSET = utf8 COMMENT ='用户操作日志';

DROP TABLE IF EXISTS `user_role_t`;
CREATE TABLE `user_role_t`
(
    `user_id`          bigint(10) NOT NULL COMMENT '用户ID',
    `role_id`          bigint(10) NOT NULL COMMENT '角色ID',
    `start_time`       datetime   NOT NULL COMMENT '开始时间',
    `end_time`         datetime    DEFAULT NULL COMMENT '结束时间',
    `create_by`        varchar(50) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime    DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   varchar(50) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime    DEFAULT NULL COMMENT '修改时间',
    UNIQUE KEY `uk_userIdRoleId` (`user_id`, `role_id`),
    KEY `idx_userId` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='用户角色表';

DROP TABLE IF EXISTS `role_permission_t`;
CREATE TABLE `role_permission_t`
(
    `role_id`          bigint(10) NOT NULL COMMENT '角色id',
    `permission_id`    bigint(10) NOT NULL COMMENT '权限id',
    `create_by`        varchar(50) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime    DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   varchar(50) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime    DEFAULT NULL COMMENT '修改时间',
    UNIQUE KEY `idx_rolePermId` (`role_id`, `permission_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='角色权限表';

DROP TABLE IF EXISTS `permission_t`;
CREATE TABLE `permission_t`
(
    `permission_id`    bigint(10)   NOT NULL AUTO_INCREMENT COMMENT '权限id',
    `resource_name`    varchar(64) NOT NULL COMMENT '资源名称',
    `operate_code`    varchar(128) NOT NULL COMMENT '操作码',
    `operate_desc`    varchar(256) DEFAULT NULL COMMENT '操作描述',
    `path`             varchar(512) DEFAULT NULL COMMENT '接口路径',
    `http_method`      varchar(16)  DEFAULT NULL COMMENT 'HTTP请求方式',
    `method_name`      varchar(256) DEFAULT NULL COMMENT '方法全路径名',
    `tenant`           varchar(32) DEFAULT NULL COMMENT '租户',
    `create_by`        varchar(50) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime    DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   varchar(50) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`permission_id`),
    UNIQUE KEY `uk_tenantResourceOpeCode` (`tenant`, `resource_name`,`operate_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='权限表';

DROP TABLE IF EXISTS `edoc_t`;
create table edoc_t
(
    id                   bigint not null AUTO_INCREMENT comment '主键ID',
    doc_no               varchar(20) not null comment '文档编号，唯一',
    doc_name             varchar(128) DEFAULT NULL comment 'doc名称',
    file_name            varchar(256) DEFAULT NULL comment '文档名称',
    doc_type             varchar(16) DEFAULT NULL comment '文档类型',
    doc_size             bigint DEFAULT NULL comment '文档大小，KB',
    download_url         varchar(256) DEFAULT NULL comment '下载地址',
    deleted              int default 0 comment '删除标志，0：未删除，1：删除',
    create_by           bigint DEFAULT NULL comment '创建人',
    creation_date        datetime DEFAULT NULL comment '创建时间',
    last_update_by       bigint DEFAULT NULL comment '更新人',
    last_update_date     datetime DEFAULT NULL comment '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_docNo` (`doc_no`)
)ENGINE = InnoDB
 AUTO_INCREMENT = 1000
 DEFAULT CHARSET = utf8 COMMENT ='文档记录表';
