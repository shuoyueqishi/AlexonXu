DROP TABLE IF EXISTS `chat_head_t`;
create table chat_head_t
(
    head_id          bigint      not null AUTO_INCREMENT comment 'head ID',
    user_id          bigint(10)  NOT NULL COMMENT 'user_id',
    chat_name        varchar(32) not null comment '对话名称',
    create_by        bigint   DEFAULT NULL comment '创建人',
    creation_date    datetime DEFAULT NULL comment '创建时间',
    last_update_by   bigint   DEFAULT NULL comment '更新人',
    last_update_date datetime DEFAULT NULL comment '更新时间',
    PRIMARY KEY (`head_id`),
    KEY `idx_chatName` (`chat_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='chat对话头表';

DROP TABLE IF EXISTS `chat_line_t`;
create table chat_line_t
(
    line_id          bigint      not null AUTO_INCREMENT comment '主键ID',
    head_id          bigint      not null comment 'head ID',
    chat_role        varchar(32) not null comment '对话角色',
    chat_content     text     default null comment '对话内容',
    create_by        bigint   DEFAULT NULL comment '创建人',
    creation_date    datetime DEFAULT NULL comment '创建时间',
    last_update_by   bigint   DEFAULT NULL comment '更新人',
    last_update_date datetime DEFAULT NULL comment '更新时间',
    PRIMARY KEY (`line_id`),
    KEY `idx_headId` (`head_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='chat对话行表';

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
    `create_by`      varchar(50)  DEFAULT NULL COMMENT '操作人',
    `creation_date`  datetime     DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_name` (`create_by`),
    KEY `idx_operate_module` (`operate_module`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 413
  DEFAULT CHARSET = utf8 COMMENT ='用户操作日志';

DROP TABLE IF EXISTS `exception_log_t`;
CREATE TABLE `exception_log_t`
(
    `id`            int(10)       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `msg`           varchar(1024) NOT NULL COMMENT '异常信息',
    `stack_trace`   text COMMENT '异常堆栈信息',
    `create_by`     bigint(10) DEFAULT NULL COMMENT '创建人',
    `creation_date` datetime      NOT NULL COMMENT '异常发生时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='异常信息日志表';

DROP TABLE IF EXISTS `open_api_key_t`;
CREATE TABLE `open_api_key_t`
(
    id             bigint(10)       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    user_id          bigint(10)  NOT NULL COMMENT 'user_id',
    api_key          varchar(64) not null comment 'OPEN AI API Key',
    create_by        bigint   DEFAULT NULL comment '创建人',
    creation_date    datetime DEFAULT NULL comment '创建时间',
    last_update_by   bigint   DEFAULT NULL comment '更新人',
    last_update_date datetime DEFAULT NULL comment '更新时间',
    PRIMARY KEY (id),
    KEY idx_userId(user_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='OpenAI API Key';
