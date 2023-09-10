package com.xlt.sharding.keygenerator;

import com.alexon.distributed.id.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;

import java.util.Properties;

@Slf4j
public class BeautySnowflakeKeyAlgorithm implements KeyGenerateAlgorithm {

    private Properties properties;

    /**
     * Get type.
     *
     * @return type
     */
    @Override
    public String getType() {
        return "BEAUTY_SNOWFLAKE";
    }

    /**
     * Generate key.
     *
     * @return generated key
     */
    @Override
    public Comparable<?> generateKey() {
        Long nextId = SnowflakeIdGenerator.getInstance().nextId();
        log.info("nextId={}", nextId);
        return nextId;
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
     * Initialize SPI.
     *
     * @param props properties to be initialized
     */
    @Override
    public void init(Properties props) {
        this.properties = props;
    }
}
