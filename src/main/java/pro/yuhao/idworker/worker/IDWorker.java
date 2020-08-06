package pro.yuhao.idworker.worker;


public class IDWorker {

    /**
     * 2020-01-01 00:00:00
     * ID生成器的零时
     * 可以不用记录剩下的50年（1970 ~ now）
     */
    private final static long START_EPOCH = 1577808000000L;

    /**
     * 业务字段长度
     */
    private static final int BIZ_LEN = 7;

    /**
     * 服务节点ID长度
     * 不同节点间ID不同
     */
    private static final int WORKER_LEN = 7;

    /**
     * 递增序列号长度
     */
    private static final int SEQ_LEN = 8;

    /**
     * 时间戳的左移位数
     */
    private static final int TIMESTAMP_SHIFT = BIZ_LEN + WORKER_LEN + SEQ_LEN;

    /**
     * 业务字段的左移位数
     */
    private static final int BIZ_SHIFT = WORKER_LEN + SEQ_LEN;

    /**
     * 服务节点的左移位数
     */
    private static final int WORKER_SHIFT = SEQ_LEN;

    /**
     * 业务ID的最大值
     */
    private static final int BIZ_MASK = (1 << BIZ_LEN) - 1;

    /**
     * 服务节点的最大值
     */
    private static final int WORKER_MASK = (1 << WORKER_LEN) - 1;

    /**
     * 序列号最大值
     */
    private static final int SEQ_MASK = (1 << SEQ_LEN) - 1;

    /**
     * 业务ID
     */
    private int bizId;
    /**
     * 服务节点ID
     */
    private int workerId;
    /**
     * 序列号
     */
    private int seq;
    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp;

    /**
     * ID生成器
     *
     * @param bizId    业务ID
     * @param workerId 节点ID
     */
    public IDWorker(int bizId, int workerId) {
        this.bizId = bizId;
        this.workerId = workerId;
    }

    /**
     * 生成ID
     *
     * @return 生成的ID
     */
    public synchronized long nextID() {
        long currTime = timeGen();
        if (currTime == lastTimestamp) {
            if (((seq + 1) & SEQ_MASK) == 0) {
                currTime = untilNextMilli();
                seq = 0;
            } else {
                seq++;
            }
        } else {
            seq = 0;
        }
        lastTimestamp = currTime;
        return ((lastTimestamp - START_EPOCH) << TIMESTAMP_SHIFT)
                | (bizId << BIZ_SHIFT)
                | (workerId << WORKER_SHIFT)
                | seq;
    }


    private long timeGen() {
        long now = System.currentTimeMillis();
        // 时钟回退抛出异常
        if (now < lastTimestamp) {
            throw new RuntimeException("Clock Backwards!");
        }
        return now;
    }

    public static long generateTimeInMillis(long id) {
        return (id >> TIMESTAMP_SHIFT) + START_EPOCH;
    }

    public static int bizId(long id) {
        return (int) (id >> BIZ_SHIFT & BIZ_MASK);
    }

    public static int workId(long id) {
        return (int) (id >> WORKER_SHIFT & WORKER_MASK);
    }


    private long untilNextMilli() {
        long next;
        do {
            next = timeGen();
        } while (next <= lastTimestamp);
        return next;
    }

    public int getBizId() {
        return bizId;
    }

    public int getWorkerId() {
        return workerId;
    }
}



