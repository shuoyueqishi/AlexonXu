DROP TABLE IF EXISTS `exception_log_t`;
create table exception_log_t
(
    id            bigint       not null AUTO_INCREMENT comment '主键ID',
    msg           varchar(512) not null comment '异常信息',
    stack_trace   text     DEFAULT NULL comment '异常堆栈信息',
    create_by     bigint   DEFAULT NULL comment '创建人',
    creation_date datetime DEFAULT NULL comment '创建时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB
 AUTO_INCREMENT = 1000
 DEFAULT CHARSET = utf8 COMMENT ='异常日志记录表';