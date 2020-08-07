package pro.yuhao.idworker.allocator.redis;

import pro.yuhao.idworker.allocator.WorkerIdAllocator;
import redis.clients.jedis.Jedis;

/**
 * 从set取出或者放回id
 */
public class RedisSetWorkerIdAllocator implements WorkerIdAllocator {

    private static final String ID_SET_PREFIX = "ID_SET_";

    /**
     * 数据源
     * 可用其他的替代
     */
    private Jedis jedis;

    public RedisSetWorkerIdAllocator(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public int allocate(String app, int bizId) {
        return Integer.parseInt(jedis.srandmember(ID_SET_PREFIX + app));
    }

    @Override
    public void recycle(String app, int bizId, int workerId) {
        jedis.sadd(ID_SET_PREFIX + app, workerId + "");
    }
}
