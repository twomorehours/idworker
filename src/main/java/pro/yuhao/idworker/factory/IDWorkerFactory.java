package pro.yuhao.idworker.factory;

import pro.yuhao.idworker.allocator.WorkerIdAllocator;
import pro.yuhao.idworker.worker.IDWorker;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * id生成器工厂
 */
public class IDWorkerFactory {

    private Map<Integer, IDWorker> workers = new ConcurrentHashMap<>();

    /**
     * 节点id分配器
     */
    private WorkerIdAllocator allocator;

    /**
     * 服务名
     */
    private String app;

    /**
     * 节点id
     */
    private int workerId = -1;


    public IDWorkerFactory(String app, WorkerIdAllocator allocator) {
        this.allocator = allocator;
        this.app = app;
    }

    /**
     * 创建ID生成器
     * 服务启动时创建
     *
     * @param bizId 业务id
     * @return
     */
    public synchronized IDWorker create(int bizId) {
        if (workerId == -1) {
            workerId = allocator.allocate(app);
        }
        IDWorker idWorker = workers.get(bizId);
        if (idWorker == null) {
            idWorker = new IDWorker(bizId, workerId);
            workers.put(bizId, idWorker);
        }
        return idWorker;
    }

    /**
     * 交给spring管理
     * 或者服务shutdown自行调用
     */
    @PreDestroy
    public void recycleIDWorker() {
        if (workerId != -1) {
            allocator.recycle(app, workerId);
        }
    }
}
