reactor反应堆模型  

- cn.nio.indigenous： 使用jdk原生接口开发
  - reactor: 主从reactor模型
  - SingleThreadNioServer：单线程模型
- cn.nio.netty： 使用netty开发的版本

启动压测进程 
```shell
./pressure.sh
```

批量关闭压测进程

```shell
ps -ef |grep NormalClient | awk '{print $2}'| xargs kill -9
```