package com.techsophy.idgenerator;


import com.techsophy.idgenerator.exception.InvalidTimeException;
import com.techsophy.idgenerator.exception.NodeLimitException;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

public class IdGeneratorImpl
{
    private static final int EPOCH_BITS = 41;
    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;
    private static final long DEFAULT_CUSTOM_EPOCH = 1420070400000L;
    private final long nodeId;
    private final long customEpoch;
    private volatile long lastTimestamp = -1L;
    private volatile long minSequence = 0L;

    public IdGeneratorImpl(long nodeId, long customEpoch)
    {
        if(nodeId < 0 || nodeId > MAX_NODE_ID)
        {
            throw new NodeLimitException(String.format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
        }
        this.nodeId = nodeId;
        this.customEpoch = customEpoch;
    }

    public IdGeneratorImpl(long nodeId)
    {
        this(nodeId, DEFAULT_CUSTOM_EPOCH);
    }

    public IdGeneratorImpl()
    {
        this.nodeId = createNodeId();
        this.customEpoch = DEFAULT_CUSTOM_EPOCH;
    }

    public synchronized BigInteger nextId()
    {
        long currentTimestamp = timestamp();
        if(currentTimestamp < lastTimestamp)
        {
            throw new InvalidTimeException("Invalid System Clock!");
        }
        if (currentTimestamp == lastTimestamp)
        {
            minSequence = (minSequence + 1) & MAX_SEQUENCE;
            if(minSequence == 0)
            {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else
        {
            minSequence = 0;
        }

        lastTimestamp = currentTimestamp;
        long id=currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS)
                          | (nodeId << SEQUENCE_BITS)
                          | minSequence;
        return BigInteger.valueOf(id);
    }

    private long timestamp()
    {
        return Instant.now().toEpochMilli() - customEpoch;
    }

    private long waitNextMillis(long currentTimestamp)
    {
        while (currentTimestamp == lastTimestamp)
        {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

    private long createNodeId()
    {
        long nodeid;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null)
                {
                    for(byte macPort: mac)
                    {
                        sb.append(String.format("%02X", macPort));
                    }
                }
            }
            nodeid = sb.toString().hashCode();
        }
        catch (Exception ex)
        {
            nodeid = (new SecureRandom().nextInt());
        }
        nodeid = nodeid & MAX_NODE_ID;
        return nodeid;
    }

    public Map<String,Long> parse(long id)
    {
        Map<String,Long> parsedObj=new HashMap<>();
        long maskNodeId = ((1L << NODE_ID_BITS) - 1) << SEQUENCE_BITS;
        long maskSequence = (1L << SEQUENCE_BITS) - 1;
        long timestamp = (id >> (NODE_ID_BITS + SEQUENCE_BITS)) + customEpoch;
        long nodeNo = (id & maskNodeId) >> SEQUENCE_BITS;
        long sequence = id & maskSequence;
        parsedObj.put("timeStamp",timestamp);
        parsedObj.put("nodeId",nodeNo);
        parsedObj.put("sequence",sequence);
        return parsedObj;
    }

    @Override
    public String toString()
    {
        return "Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS
                + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch
                + ", NodeId=" + nodeId + "]";
    }
}
