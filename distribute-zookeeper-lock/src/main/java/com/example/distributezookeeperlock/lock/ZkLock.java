package com.example.distributezookeeperlock.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/*
  @Classname ZkLock
  @Description TODO
  @Date 2021/9/27 21:29
  @Created by lc
 */

@Slf4j
public class ZkLock implements Watcher,AutoCloseable {

    private ZooKeeper zooKeeper;
    //ip+port
    private String connectString = "localhost:2181";
    //会话超时时间,单位是毫秒
    private int sessionTimeout = 10000;
    //观察器，传入自身就行
    private Watcher watcher = this;
    //节点
    private String zNode;
    //构造函数
    public ZkLock(){
        try {
            this.zooKeeper = new ZooKeeper(connectString,sessionTimeout,watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取锁
    public boolean getLock(String bussinessCode){
        //根据业务区分锁
        try {
            //创建业务根节点  /order
            String path = "/" + bussinessCode;
            Stat exists = zooKeeper.exists(path, false);//添加监听器
            if(exists==null){
                //创建节点,ZooDefs.Ids.OPEN_ACL_UNSAFE不使用密码连接zookeeper
                //CreateMode创建节点的模式--选择持久节点
                zooKeeper.create(path,bussinessCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            //创建瞬时有序节点 /order/order_
             zNode = zooKeeper.create(path + path + "_", bussinessCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

             //找到所有的瞬时节点,不添加观察器
            List<String> childrenNodes = zooKeeper.getChildren(path, false);
            Collections.sort(childrenNodes);
            //找到当前序号最小的子节点
            String firstNode = childrenNodes.get(0);
            //zNode = /order/order_00000001
            if(zNode.endsWith(firstNode)){
                //如果当前节点是编号最小的瞬时节点，就拿到锁
                return true;
            }
            //01->02->03
            //不是第一个子节点，则监听前一个节点
            String lastNode = firstNode;//从当前的第一个节点开始监听
            for(String node:childrenNodes){
                //找到当前的节点，判断它的前一个节点是否存在
                if(zNode.endsWith(node)){
                    //判断lastNode是否已经消失了
                    //设置了监听器，一旦这个节点消失了，就会进入process方法
                    zooKeeper.exists(path + "/" + lastNode, true);
                    break;
                }else{
                    lastNode = node;
                }
            }
            //多个线程使用这个对象，线程等待获取节点的通知，进而获得锁
            synchronized (this){
                wait();
            }
            return true;


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        //自动关闭
        //String path =
        zooKeeper.delete(zNode,-1);
        log.info("释放锁");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //节点删除要进入的函数
        if(watchedEvent.getType()==Event.EventType.NodeDeleted){
            //唤起等待线程
            synchronized (this){
                notify();
            }

        }

    }
}
