package admin.demo.Service;

import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;

public interface IConditionerService {
    /**
     * 房间发出服务请求时调用
     输入：请求记录record
     作用：尝试进行送风服务，若当前无法服务则进入等待队列
     **/
    public void ServiceRequest(Record record);
    /**
     * 管理员或房间关机时调用
     输入：房间ID roomId
     作用：将某个空调移出等待队列或服务队列
     **/
    public Conditioner StopRequest(Integer roomId);
    /**
     * 用户退房时调用
     输入：用户ID userId
     作用：关掉房间的空调，存储总账单，设置用户退出时间
     **/
    public void CheckOut(Integer userId);

    /*
     * 周期性调度，每隔1分钟调用
     作用：对当前空调及房间状态进行更新
     */
    //public void Schedule();
    /*
     * 房间发出停止服务请求时调用
     输入：房间ID roomId
     作用：将空调移出队列，设置为待机状态
     */
    //void StandByRequest(Integer roomId);
}
