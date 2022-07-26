CREATE DEFINER=`root`@`localhost` PROCEDURE `create_member_datas`(IN `totalRecords` int)
BEGIN
    DECLARE i INT DEFAULT 0 ; -- 定义变量
    DECLARE _telephone BIGINT(11);
    DECLARE _card_number varchar(10);
    DECLARE _name varchar(60) ;
    DECLARE _payment_type int(4);
    DECLARE _temNum BIGINT(10);
    SET _telephone=18272069216;
    SET _card_number='10000';
    SET _name='Member_';
    SET _payment_type=0;
    SELECT max(id) INTO i FROM member_t;
    IF i IS NULL
    THEN SET i = 0;
    END IF;
    SET totalRecords=totalRecords+i;
    WHILE i < totalRecords DO  -- 符合条件就循环
    -- 核心循环SQL;
            SET i = i + 1 ; -- 计数器+1
            SET _telephone = _telephone+3;
            SET _temNum=CONVERT(_card_number,UNSIGNED);
            SET _temNum=_temNum+1;
            SET _card_number = CONVERT(_temNum, CHAR);
            SET _name = CONCAT(_name,i);
            SET _payment_type = i mod 5;
            INSERT INTO `member_t`
            ( `id`,
              `card_number`,
              `name`,
              `birthday`,
              `telephone`,
              `integral`,
              `money`,
              `payment_type`,
              `address`,
              `create_by`,
              `creation_date`,
              `last_update_by`,
              `last_update_date` )
            VALUES
            ( i,
              _card_number,
              _name,
              '01-22',
              _telephone,
              100,
              1000,
              _payment_type,
              '龙岗区雪岗路2057',
              'Alex',
              SYSDATE(),
              'xulitao',
              SYSDATE() );
            SET _name='Member_';
        END WHILE;
END