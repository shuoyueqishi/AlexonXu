DROP TABLE IF EXISTS `order_t`;
CREATE TABLE `order_t`
(
    `order_id`         bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'order_id主键',
    `order_no`         varchar(32)    DEFAULT NULL COMMENT '订单编号',
    `user_id`          bigint(10) NOT NULL COMMENT '用户ID',
    `amount`           decimal(20, 4) DEFAULT NULL COMMENT '订单金额',
    `status`           tinyint(4)     DEFAULT 0 COMMENT '状态，0：草稿，1：已提交，2：已付款，3：待发货，4：已发货，5：已收货，6：已完成',
    `deleted`          tinyint(4)     DEFAULT 0 COMMENT '删除标志，0：未删除，1：已删除',
    `create_by`        bigint(10)     DEFAULT NULL COMMENT '创建人',
    `creation_date`    bigint(13)     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)     DEFAULT NULL COMMENT '修改人',
    `last_update_date` bigint(13)     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`order_id`),
    KEY `idx_useId` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='订单表';

DROP TABLE IF EXISTS `product_t`;
CREATE TABLE `product_t`
(
    `product_id`       bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
    `product_code`     varchar(32)    DEFAULT NULL COMMENT '商品编码',
    `product_name`     varchar(128)   DEFAULT NULL COMMENT '商品名称',
    `product_desc`     varchar(256)   DEFAULT NULL COMMENT '商品描述',
    `price`            decimal(20, 4) DEFAULT NULL COMMENT '价格',
    `stock`            bigint(10)     DEFAULT NULL COMMENT '库存',
    `status`           tinyint(4)     DEFAULT 0 COMMENT '状态，0：待上架，1：上架中，2：已下架',
    `deleted`          tinyint(4)     DEFAULT 0 COMMENT '删除标志，0：未删除，1：已删除',
    `create_by`        bigint(10)     DEFAULT NULL COMMENT '创建人',
    `creation_date`    bigint(13)     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)     DEFAULT NULL COMMENT '修改人',
    `last_update_date` bigint(13)     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`product_id`),
    UNIQUE KEY `uk_productCode` (`product_code`),
    KEY `idx_productName` (`product_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='商品表';

DROP TABLE IF EXISTS `order_product_delivery_t`;
CREATE TABLE `order_product_delivery_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `order_id`         bigint(10)  DEFAULT NULL COMMENT '订单ID',
    `product_id`       bigint(10)  DEFAULT NULL COMMENT '商品ID',
    `quantity`         bigint(10)  DEFAULT 0 COMMENT '购买数量',
    `delivery_type`    tinyint(4)  DEFAULT NULL COMMENT '快递类型，1：顺丰，2：圆通，3：中通，4：韵达，5：优速，6：极兔，7：EMS',
    `delivery_no`      varchar(32) DEFAULT NULL COMMENT '快递单号',
    `create_by`        bigint(10)  DEFAULT NULL COMMENT '创建人',
    `creation_date`    bigint(13)  DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)  DEFAULT NULL COMMENT '修改人',
    `last_update_date` bigint(13)  DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_orderProductDelivery` (`order_id`, `product_id`, `delivery_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='订单商品物流表';

DROP TABLE IF EXISTS `delivery_t`;
CREATE TABLE `delivery_t`
(
    `delivery_id`      bigint(10)  NOT NULL AUTO_INCREMENT COMMENT '物流ID',
    `delivery_type`    tinyint(4)  NOT NULL COMMENT '快递类型，1：顺丰，2：圆通，3：中通，4：韵达，5：优速，6：极兔，7：EMS',
    `delivery_no`      varchar(32) NOT NULL COMMENT '快递单号',
    `status`           tinyint(4) DEFAULT 0 COMMENT '快递状态，0：已揽件，1：运输中，2：投递中，3：已投递',
    `create_by`        bigint(10) DEFAULT NULL COMMENT '创建人',
    `creation_date`    bigint(13) DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10) DEFAULT NULL COMMENT '修改人',
    `last_update_date` bigint(13) DEFAULT NULL COMMENT '修改时间',
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
    `creation_date`      bigint(13)   DEFAULT NULL COMMENT '创建时间',
    `last_update_by`     bigint(10)   DEFAULT NULL COMMENT '修改人',
    `last_update_date`   bigint(13)   DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`delivery_detail_id`),
    KEY `idx_deliveryId` (`delivery_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='快递流转详细信息表';