package com.xlt.sharding.startegy;

import com.alexon.exception.CommonException;
import com.alexon.exception.utils.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.xlt.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DateStdShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    private Date lowerDate;

    private Date upperDate;

    private Integer interval;

    private Properties properties;


    @Override
    public void init(Properties properties) {
        String lowerLimit = (String) properties.get("lowerLimit");
        String upperLimit = (String) properties.get("upperLimit");
        String interval = (String) properties.get("interval");
        AssertUtil.isNull(lowerLimit, "lowerLimit can't be empty");
        AssertUtil.isNull(upperLimit, "upperLimit can't be empty");
        AssertUtil.isNull(interval, "interval can't be empty");
        this.lowerDate = DateUtil.parseDate(lowerLimit);
        this.upperDate = DateUtil.parseDate(upperLimit);
        this.interval = Integer.parseInt(interval);
        this.properties = properties;
    }



    /**
     * Get properties.
     *
     * @return properties
     */
    @Override
    public Properties getProps() {
        return properties;
    }

    /**
     * Get type.
     *
     * @return type
     */
    @Override
    public String getType() {
        return "QUARTER_DATE";
    }

    /**
     * Sharding.
     *
     * @param tableNames    available data sources or table names
     * @param shardingValue sharding value
     * @return sharding result for data source or table name
     */
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Date> shardingValue) {
        log.info("tableNames={},shardingValue={},properties={}", JSON.toJSONString(tableNames), JSON.toJSONString(shardingValue), JSON.toJSONString(properties));
        Date date = shardingValue.getValue();
        AssertUtil.isTrue(date.getTime() < lowerDate.getTime(), DateUtil.formatDate(date) + " is before lowerLimit: " + DateUtil.formatDate(lowerDate));
        AssertUtil.isTrue(date.getTime() > upperDate.getTime(), DateUtil.formatDate(date) + " is after upperLimit: " + DateUtil.formatDate(upperDate));
        int idx = calTableIdx(date);
        log.info("idx={}", idx);
        String targetTable = "";
        for (String tableName : tableNames) {
            String tblIdxStr = tableName.substring(tableName.indexOf("t") + 1);
            int tblIdx = Integer.parseInt(tblIdxStr);
            if (tblIdx == idx) {
                targetTable = tableName;
                break;
            }
        }
        log.info("targetTable={}", targetTable);
        return targetTable;
    }

    private int calTableIdx(Date date) {
        int months = (DateUtil.getYear(date) - DateUtil.getYear(lowerDate)) * 12 + DateUtil.getMonth(date);
        int flag = months % interval == 0 ? 0 : 1;
        return months / interval + flag;
    }

    /**
     * Sharding.
     *
     * @param tableNames    available data sources or table names
     * @param shardingValue sharding value
     * @return sharding results for data sources or table names
     */
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Date> shardingValue) {
        log.info("tableNames={},shardingValue={},properties={}", JSON.toJSONString(tableNames), JSON.toJSONString(shardingValue), JSON.toJSONString(properties));
        Date lowDate = shardingValue.getValueRange().lowerEndpoint();
        Date upDate = shardingValue.getValueRange().upperEndpoint();
        AssertUtil.isTrue(lowDate.getTime() > upDate.getTime(), DateUtil.formatDate(lowDate) + " is after upperEndpoint: " + DateUtil.formatDate(upDate));
        AssertUtil.isTrue(lowDate.getTime() < lowerDate.getTime(), DateUtil.formatDate(lowDate) + " is before lowerLimit: " + DateUtil.formatDate(lowerDate));
        AssertUtil.isTrue(upDate.getTime() > upperDate.getTime(), DateUtil.formatDate(upDate) + " is after upperLimit: " + DateUtil.formatDate(upperDate));
        int lowIdx = calTableIdx(lowDate);
        int upIdx =  calTableIdx(upDate);
        log.info("lowIdx={},upIdx={}", lowIdx, upIdx);
        List<String> targetTbls = new ArrayList<>();
        for (String tableName : tableNames) {
            String dsIdxStr = tableName.substring(tableName.indexOf("t") + 1);
            int dsIdx = Integer.parseInt(dsIdxStr);
            if (dsIdx >= lowIdx && dsIdx <= upIdx) {
                targetTbls.add(tableName);
            }
        }
        log.info("target table names={}", targetTbls);
        return targetTbls;
    }
}
