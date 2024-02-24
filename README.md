reactor反应堆模型  

- cn.nio.indigenous： 使用jdk原生接口开发
  - reactor 包: 主从reactor模型
  - SingleReactorSingleThreadServer：单线程模型
- cn.nio.netty： 使用netty开发的版本

启动压测进程 
```shell
./pressure.sh
```

批量关闭压测进程

```shell
ps -ef |grep NormalClient | awk '{print $2}'| xargs kill -9
```

#### 主从reactor响应堆模式，为什么要有主reactor和从reactor，一个reactor不行吗？
主从 Reactor 响应堆模式是一种多线程的网络编程模式，用于处理大量的并发网络连接。在该模式中，主 Reactor 负责监听所有的连接请求，而从 Reactor 负责处理已经建立连接的数据传输。主从 Reactor 模式的主要目的是提高系统的并发性能和可扩展性。

一个 Reactor 能够处理大量的并发连接，但是在高并发的情况下，一个 Reactor 可能会成为系统的瓶颈。如果所有的连接都由一个 Reactor 处理，那么当连接数量增加时，Reactor 将不得不处理更多的连接请求，这可能会导致性能下降。此外，如果出现故障，单个 Reactor 可能会导致整个系统崩溃。

因此，主从 Reactor 响应堆模式将连接请求的监听和数据传输的处理分别交给主 Reactor 和从 Reactor，以提高系统的性能和可靠性。主 Reactor 负责监听所有的连接请求，并将新连接分配给从 Reactor 处理。从 Reactor 负责处理已经建立连接的数据传输，从而减轻主 Reactor 的负载。这种模式可以充分利用多核 CPU 的优势，提高系统的并发性能和可扩展性。