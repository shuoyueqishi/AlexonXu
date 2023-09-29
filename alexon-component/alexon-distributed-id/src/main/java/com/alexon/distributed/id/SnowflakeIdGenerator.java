package com.alexon.distributed.id;

import com.alexon.distributed.id.mapper.ISnowflakeWorkerIdMapper;
import com.alexon.distributed.id.po.SnowflakeWorkerIdPo;
import com.alexon.model.utils.AppContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
@Slf4j
public class SnowflakeIdGenerator {

    /**
     * 开始时间截 (2021-01-06)
     */
    private final long twepoch = 1609944039799L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 9L;

    /**
     * 数据标识id所占的位数
     */
    private final long dataCenterIdBits = 1L;

    /**
     * 支持的最大机器id = 2^workerIdBits
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);

    /**
     * 支持的最大数据标识id = 2^dataCenterIdBits
     */
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 工作机器ID(0~1024)
     */
    private long workerId;

    /**
     * 数据中心ID(0)
     */
    private long dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 实例
     */
    private volatile static SnowflakeIdGenerator INSTANCE;

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static SnowflakeIdGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized (SnowflakeIdGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SnowflakeIdGenerator();
                }
            }
        }
        return INSTANCE;
    }

    public SnowflakeIdGenerator() {
        long workerId = getWorkerId();
        long dataCenterId = getDataCenterId();
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized Long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 从雪花id中计算出规定起始时间戳的时间戳
     * 起始时间戳见EPOCH字段
     *
     * @param snowId 雪花算法生成的id
     * @return 雪花算法ID对应的时间
     */
    public long getTimestamp(long snowId) {
        String bid = Long.toBinaryString(snowId);
        int timestamp = bid.length() - (int)(workerIdBits + dataCenterIdBits + sequenceBits);
        String substring = bid.substring(0, timestamp);
        return Long.parseUnsignedLong(substring, 2)+twepoch;
    }

    /**
     * 根据雪花id中计算出生成ID时的时间
     *
     * @param snowId 雪花算法生成的id
     * @return 时间
     */
    public Date getDate(long snowId) {
        long timestamp = getTimestamp(snowId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }

    /**
     * 根据雪花id中计算出生成ID时的时间字符串
     *
     * @param snowId 雪花算法生成的id
     * @return 时间字符串
     */
    public String getDateStr(long snowId) {
        Date date = getDate(snowId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    private long getWorkerId() {
        ISnowflakeWorkerIdMapper workerIdMapper = AppContextUtil.getBean(ISnowflakeWorkerIdMapper.class);
        SnowflakeWorkerIdPo workerIdPo = new SnowflakeWorkerIdPo();
        workerIdPo.setLastUpdateBy(-1L);
        workerIdPo.setCreationDate(new Date());
        workerIdPo.setLastUpdateBy(-1L);
        workerIdPo.setLastUpdateDate(new Date());
        workerIdMapper.insert(workerIdPo);
        Long id = workerIdPo.getId();
        int workerTotalNum = 1 << this.workerIdBits;
        long workerId = id % workerTotalNum;
        log.info("workerId={}", workerId);
        return workerId;
    }

    private long getDataCenterId() {
        int sums = 0;
        int dateCenterTotalNum = 1 << this.dataCenterId;
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            for (int b : ints) {
                sums += b;
            }
        } catch (UnknownHostException e) {
            log.error("get local Host Address failed:", e);
            return RandomUtils.nextLong(0, dateCenterTotalNum);
        }
        return sums % dateCenterTotalNum;
    }
}
