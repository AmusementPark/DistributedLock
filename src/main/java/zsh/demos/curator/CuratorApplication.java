package zsh.demos.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class CuratorApplication implements DisposableBean, InitializingBean {

    private ExecutorService executorService;
    @Autowired
    private ZkDistributedLock zkDistributedLock;

    public static void main(String[] args) throws Exception {
        CuratorApplication app = SpringApplication.run(CuratorApplication.class, args).getBean(CuratorApplication.class);
        app.interProcessMutex();
        app.interProcessMutex();
    }

//    private void tryConcurrentDistributedLock() {
//        Runnable lock = () -> {
////            while (true) {
//                log.info("--------------------------------------------------");
//                    zkDistributedLock.acquireDistributedLock("t");
//            zkDistributedLock.acquireDistributedLock("t");
////                    zkDistributedLock.releaseDistributedLock("t");
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
//        };
//        Runnable unlock = () -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            zkDistributedLock.releaseDistributedLock("t");
//        };
//        executorService.submit(lock);
//        executorService.submit(unlock);
//    }

    @Autowired
    private CuratorFramework curatorFramework;

    private void interProcessMutex() throws Exception {
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, "/lock-path");
        try {
            interProcessMutex.acquire();
        } finally {
            interProcessMutex.release();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executorService = Executors.newFixedThreadPool(20);
    }

    @Override
    public void destroy() throws Exception {
        this.executorService.shutdown();
    }
}

