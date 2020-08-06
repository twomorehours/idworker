package pro.yuhao.idworker.allocator;


public interface WorkerIdAllocator {

    /**
     * 分配ID生成器服务节点ID
     *
     * @param bizId 业务ID 用不用取决于具体的实现
     * @return 分配的节点id
     */
    int allocate(int bizId);

    /**
     * 回收服务节点id
     *
     * @param bizId    业务ID 用不用取决于具体的实现
     * @param workerId 服务节点id
     */
    void recycle(int bizId, int workerId);
}
