package com.xlt.sharding.startegy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class DateRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    @Override
    public Collection<String> doSharding(Collection<String> dataDbs, RangeShardingValue<Date> rangeShardingValue) {
        log.info("dataDbs={}", JSON.toJSONString(dataDbs));
        log.info("rangeShardingValue={}", JSON.toJSONString(rangeShardingValue));
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String lowerDate = dateFormat.format(valueRange.lowerEndpoint());
        String upperDate = dateFormat.format(valueRange.upperEndpoint());
        if (Integer.getInteger(lowerDate) < 202200) {
            lowerDate = "202201";
        }
        if (Integer.getInteger(upperDate) > 202306) {
            upperDate = "202306";
        }
        int lowerIdx = (Integer.getInteger(lowerDate) - 202200) / 3;
        int upperIdx = (Integer.getInteger(upperDate) - 202200) / 3;

        List<String> targetDbs = new ArrayList<>();
        for (String ds : dataDbs) {
            String dsIdxStr = ds.substring(ds.indexOf("_"));
            Integer dsIdx = Integer.valueOf(dsIdxStr);
            if (dsIdx >= lowerIdx && dsIdx <= upperIdx) {
                targetDbs.add(ds);
            }
        }
        log.info("target data source={}", targetDbs);
        return targetDbs;
    }
}
