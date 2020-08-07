package pro.yuhao.idworker;

import pro.yuhao.idworker.allocator.WorkerIdAllocator;
import pro.yuhao.idworker.allocator.redis.RedisSetWorkerIdAllocator;
import pro.yuhao.idworker.factory.IDWorkerFactory;
import pro.yuhao.idworker.worker.IDWorker;
import redis.clients.jedis.Jedis;

public class IdWorkerTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("106.12.15.56", 6379, 3000);

        WorkerIdAllocator workerIdAllocator = new RedisSetWorkerIdAllocator(jedis);

        IDWorkerFactory idWorkerFactory = new IDWorkerFactory("test_app",workerIdAllocator);

        IDWorker idWorker = idWorkerFactory.create(1);

        System.out.println("bizId: " + idWorker.getBizId());

        System.out.println("workerId: " + idWorker.getWorkerId());

        System.out.println("time: " + System.currentTimeMillis());

        long id = idWorker.nextID();

        System.out.println("=========================");
        System.out.println("id: " + id);
        System.out.println("=========================");

        System.out.println("bizId from id: " + IDWorker.bizId(id));

        System.out.println("workerId from id: " + IDWorker.workId(id));

        System.out.println("time from id: " + IDWorker.generateTimeInMillis(id));


        idWorkerFactory.recycleIDWorker();
    }
}
