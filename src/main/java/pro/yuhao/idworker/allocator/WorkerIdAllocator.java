package pro.yuhao.idworker.allocator;


public interface WorkerIdAllocator {

    /**
     * 分配ID生成器服务节点ID
     *
     * @param app   服务名
     * @param bizId 业务ID 用不用取决于具体的实现
     * @return 分配的节点id
     */
    int allocate(String app, int bizId);

    /**
     * 回收服务节点id
     *
     * @param app      服务名
     * @param bizId    业务ID 用不用取决于具体的实现
     * @param workerId 服务节点id
     */
    void recycle(String app, int bizId, int workerId);
}
