DROP TABLE IF EXISTS `category_t`;
CREATE TABLE `category_t`
(
    `category_id`      int(10)      NOT NULL AUTO_INCREMENT COMMENT '主键（自增ID）',
    `code`             varchar(64)  NOT NULL COMMENT '编码',
    `name`             varchar(128) NOT NULL COMMENT '名称',
    `parent_id`        int(10)      NOT NULL DEFAULT '0' COMMENT '父级ID，默认为0',
    `sort`             int(8)                DEFAULT '0' COMMENT '分类排序',
    `status`           tinyint(4)            DEFAULT '0' COMMENT '状态(0：在用，1：停用)',
    `remarks`          varchar(255)          DEFAULT NULL COMMENT '分类备注',
    `create_by`        bigint(10)            DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime              DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)            DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime              DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`category_id`),
    UNIQUE KEY `unique_code` (`code`),
    UNIQUE KEY `unique_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='类别表';

DROP TABLE IF EXISTS `brand_t`;
CREATE TABLE `brand_t`
(
    `brand_id`         bigint(10)   NOT NULL AUTO_INCREMENT COMMENT '品牌id',
    `name`             varchar(64)  NOT NULL COMMENT '品牌名称',
    `image`            varchar(256) NULL DEFAULT '' COMMENT '品牌图片地址',
    `initial`          varchar(1)        DEFAULT '' COMMENT '品牌的首字母',
    `sort`             int(8)       NULL DEFAULT NULL COMMENT '排序',
    `create_by`        bigint(10)        DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime          DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)        DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime          DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT ='品牌类别关联表';

DROP TABLE IF EXISTS `category_brand_rel_t`;
CREATE TABLE `category_brand_rel_t`
(
    `category_id` bigint(10) NOT NULL COMMENT '分类ID',
    `brand_id`    bigint(10) NOT NULL COMMENT '品牌ID',
    PRIMARY KEY (`brand_id`, `category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='品牌类别关联表';

DROP TABLE IF EXISTS `commodity_spu_t`;
CREATE TABLE `commodity_spu_t`
(
    `spu_id`           bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
    `category_id`      int(10)    NOT NULL COMMENT '商品分类ID',
    `brand_id`         bigint(10)   DEFAULT NULL COMMENT '品牌ID',
    `code`             varchar(32)  DEFAULT NULL COMMENT '商品编码',
    `name`             varchar(128) DEFAULT NULL COMMENT '商品名称',
    `description`      varchar(512) DEFAULT NULL COMMENT '商品描述',
    `marketable`       tinyint(4)   DEFAULT 0 COMMENT '上架状态，0：未上架，2：已上架',
    `approve_status`   tinyint(4)   DEFAULT 0 COMMENT '审批状态，0：未审核，1：已审核，2：审核不通过',
    `deleted`          tinyint(4)   DEFAULT 0 COMMENT '删除标志，0：未删除，1：已删除',
    `create_by`        bigint(10)   DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)   DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`spu_id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='商品SPU表';

DROP TABLE IF EXISTS `commodity_spu_img_t`;
CREATE TABLE `commodity_spu_img_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `spu_id`           bigint(10)      DEFAULT NULL COMMENT '商品SPU id',
    `image`            varchar(256)    DEFAULT NULL COMMENT '图片路径',
    `sort`             int(8)     NULL DEFAULT NULL COMMENT '排序',
    `create_by`        bigint(10)      DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime        DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)      DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime        DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '商品SPU图片集合';


DROP TABLE IF EXISTS `commodity_sku_t`;
CREATE TABLE `commodity_sku_t`
(
    `sku_id`           bigint(10)     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `spu_id`           bigint(10)     NULL DEFAULT NULL COMMENT '商品SPU id',
    `name`             varchar(256)        DEFAULT NULL COMMENT 'SKU名称',
    `code`             varchar(35)         DEFAULT NULL COMMENT 'SKU编号',
    `description`      varchar(512)        DEFAULT NULL COMMENT '规格描述',
    `sale_volume`      int(10)             DEFAULT NULL COMMENT '销量',
    `price`            decimal(16, 2) NULL DEFAULT NULL COMMENT '价格',
    `marketable`       tinyint(4)          DEFAULT 0 COMMENT '上架状态，0：未上架，2：已上架',
    `deleted`          tinyint(4)          DEFAULT 0 COMMENT '删除标志，0：未删除，1：已删除',
    `create_by`        bigint(10)          DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime            DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)          DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime            DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`sku_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '商品规格';

DROP TABLE IF EXISTS `attribute_t`;
CREATE TABLE `attribute_t`
(
    `attr_id`          bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`             varchar(64)  DEFAULT NULL COMMENT '名称',
    `value`            varchar(256) DEFAULT NULL COMMENT '属性值',
    `status`           tinyint(4)   DEFAULT 0 COMMENT '状态, 0:生效，1：失效',
    `type`             tinyint(4)   DEFAULT 1 COMMENT '属性类型 1 规格属性 2 销售属性',
    `category_id`      bigint(10)   DEFAULT NULL COMMENT '分类id',
    `create_by`        bigint(10)   DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime     DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)   DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`attr_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '属性表';

DROP TABLE IF EXISTS `commodity_sku_attr_rel_t`;
CREATE TABLE `commodity_sku_attr_rel_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `sku_id`           bigint(10) NULL DEFAULT NULL COMMENT '商品SKU ID',
    `attr_id`          bigint(10) NULL DEFAULT NULL COMMENT '属性id',
    `sort`             int(8)     NULL DEFAULT NULL COMMENT '排序',
    `create_by`        bigint(10)      DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime        DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)      DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime        DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '商品规格SKU属性关联表';

DROP TABLE IF EXISTS `commodity_spu_attr_rel_t`;
CREATE TABLE `commodity_spu_attr_rel_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `spu_id`           bigint(10) NULL DEFAULT NULL COMMENT '商品SPU ID',
    `attr_id`          bigint(10) NULL DEFAULT NULL COMMENT '属性id',
    `sort`             int(8)     NULL DEFAULT NULL COMMENT '排序',
    `create_by`        bigint(10)      DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime        DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)      DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime        DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '商品SPU属性关联表';

DROP TABLE IF EXISTS `commodity_sku_img_t`;
CREATE TABLE `commodity_sku_img_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `sku_id`           bigint(10)      DEFAULT NULL COMMENT '商品SKU id',
    `image`            varchar(256)    DEFAULT NULL COMMENT '图片路径',
    `sort`             int(8)     NULL DEFAULT NULL COMMENT '排序',
    `create_by`        bigint(10)      DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime        DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)      DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime        DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
    COMMENT = '商品SKU图片集合';

DROP TABLE IF EXISTS `commodity_stock_t`;
CREATE TABLE `commodity_stock_t`
(
    `id`               bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `sku_id`           bigint(10) NOT NULL COMMENT '商品SKU id',
    `quantity`         int(10)    NULL DEFAULT 0 COMMENT '库存数量',
    `create_by`        bigint(10)      DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime        DEFAULT NULL COMMENT '创建时间',
    `last_update_by`   bigint(10)      DEFAULT NULL COMMENT '修改人',
    `last_update_date` datetime        DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_skuId (`sku_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT = '商品规格SKU库存';