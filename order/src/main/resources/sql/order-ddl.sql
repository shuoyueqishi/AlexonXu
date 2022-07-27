DROP TABLE IF EXISTS `order_t`;
CREATE TABLE `order_t`
(
    `order_id`         bigint(10)     NOT NULL AUTO_INCREMENT COMMENT 'order_id主键',
    `order_no`         varchar(32)    DEFAULT NULL COMMENT '订单编号',
    `user_id`          bigint(10)     NOT NULL COMMENT '用户ID',
    `order_amount`     decimal(16, 2) NOT NULL COMMENT '订单金额',
    `delivery_amount`  decimal(16, 2) DEFAULT 0 COMMENT '运费',
    `total_amount`     decimal(16, 2) NOT NULL COMMENT '汇总金额',
    `receiver_id`      bigint(10)     NOT NULL COMMENT '收货地址ID',
    `status`           tinyint(4)     DEFAULT 1 COMMENT '状态，1：已提交，2：已付款，3：待发货，4：已发货，5：已收货，6：已完成',
    `deleted`          tinyint(4)     DEFAULT 0 COMMENT '删除标志，0：未删除，1：已删除',
    `create_by`        bigint(10)     DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime       DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)     DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime       DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`order_id`),
    KEY `idx_useId` (`user_id`),
    KEY `idx_orderNo` (`order_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='订单表';

DROP TABLE IF EXISTS `receiver_info_t`;
CREATE TABLE `receiver_info_t`
(
    `receiver_id`        bigint(10)   NOT NULL AUTO_INCREMENT COMMENT 'receiver_id主键',
    `user_id`            bigint(10)   NOT NULL COMMENT '用户ID',
    `receiver_name`      varchar(64) DEFAULT NULL COMMENT '收件人姓名',
    `receiver_telephone` varchar(32)  NOT NULL COMMENT '收件人电话',
    `receiver_address`   varchar(256) NOT NULL COMMENT '收件人地址',
    `default_flag`       tinyint(4)  DEFAULT 0 COMMENT '是否默认地址，0：否，1：是',
    `create_by`          bigint(10)  DEFAULT NULL COMMENT '创建人',
    `creation_date`      datetime    DEFAULT NULL COMMENT '创建时间',
    `last_update_by`     bigint(10)  DEFAULT NULL COMMENT '修改人',
    `last_update_date`   datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`receiver_id`),
    UNIQUE KEY `uk_useId` (`user_id`, `receiver_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='用户收货地址表';

DROP TABLE IF EXISTS `order_commodity_rel_t`;
CREATE TABLE `order_commodity_rel_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `order_id`         bigint(10) DEFAULT NULL COMMENT '订单ID',
    `sku_id`           bigint(10) NOT NULL COMMENT '商品SKU id',
    `quantity`         bigint(10) DEFAULT 0 COMMENT '购买数量',
    `create_by`        bigint(10) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime   DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime   DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orderSkuId` (`order_id`, `sku_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='订单商品关联表';

DROP TABLE IF EXISTS `order_delivery_rel_t`;
CREATE TABLE `order_delivery_rel_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `order_id`         bigint(10) DEFAULT NULL COMMENT '订单ID',
    `delivery_id`      bigint(10) NOT NULL COMMENT '物流ID',
    `create_by`        bigint(10) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime   DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime   DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orderDeliveryId` (`order_id`, `delivery_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='订单物流关联表';

DROP TABLE IF EXISTS `delivery_t`;
CREATE TABLE `delivery_t`
(
    `delivery_id`      bigint(10)  NOT NULL AUTO_INCREMENT COMMENT '物流ID',
    `delivery_type`    tinyint(4)  NOT NULL COMMENT '快递类型，1：顺丰，2：圆通，3：中通，4：韵达，5：优速，6：极兔，7：EMS',
    `delivery_no`      varchar(32) NOT NULL COMMENT '快递单号',
    `status`           tinyint(4) DEFAULT 0 COMMENT '快递状态，0：已揽件，1：运输中，2：投递中，3：已投递',
    `create_by`        bigint(10) DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime   DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10) DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime   DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`delivery_id`),
    UNIQUE KEY `uk_deliveryNo` (`delivery_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='快递信息表';

DROP TABLE IF EXISTS `delivery_detail_t`;
CREATE TABLE `delivery_detail_t`
(
    `delivery_detail_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '物流详细信息表',
    `delivery_id`        bigint(10)   DEFAULT NULL COMMENT '物流ID',
    `position`           varchar(256) DEFAULT NULL COMMENT '当前位置',
    `time`               bigint(13)   DEFAULT NULL COMMENT '时间',
    `create_by`          bigint(10)   DEFAULT NULL COMMENT '创建人',
    `creation_date`      datetime     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`     bigint(10)   DEFAULT NULL COMMENT '修改人',
    `last_update_date`   datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`delivery_detail_id`),
    KEY `idx_deliveryId` (`delivery_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='快递流转详细信息表';
