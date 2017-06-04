package com.github.dozzatq.phoenix.Tasks;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by rodeon on 5/26/17.
 */

public class Tasks {
    private static ThreadPoolExecutor threadPoolExecutor;
    static {
        int numCores = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(numCores * 2, numCores *2,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }


    public static <PResult,ZResult> RuntimeTask<PResult,ZResult>
        executeRuntime(final RuntimeTaskSource<PResult,ZResult> taskSource)
    {
        return executeRuntime(getDefaultExecutor(), taskSource);
    }

    public static Executor getDefaultExecutor()
    {
        return threadPoolExecutor;
    }

    public static <PResult,ZResult> RuntimeTask<PResult,ZResult>
        executeRuntime(Executor executor, final RuntimeTaskSource<PResult,ZResult> taskSource)
    {
        try {
            taskSource.setPoolExecutor((ThreadPoolExecutor) executor);
        }
        catch (ClassCastException e)
        {

        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    taskSource.setResult(taskSource.call());
                } catch (Exception e) {
                    taskSource.setException(e);
                }
            }
        });
        return taskSource.getTask();
    }

    public static <PResult> PResult await(@NonNull Task<PResult> task) throws ExecutionException,InterruptedException{
        if (Looper.getMainLooper()==Looper.myLooper())
            throw new IllegalStateException("await must not be called on the main thread");
        if(task == null) {
            throw new NullPointerException("Task must not be null");
        }
        if (task.isComplete())
            return getResultTask(task);
        else {
            WaiterTask waiterTask = new WaiterTask();
            task.addOnSuccessListener(DefaultExecutor.CURRENT_THREAD_EXECUTOR, waiterTask);
            task.addOnFailureListener(DefaultExecutor.CURRENT_THREAD_EXECUTOR, waiterTask);
            waiterTask.await();
            return getResultTask(task);
        }
    }

    private static <TResult> TResult getResultTask(Task<TResult> task) throws ExecutionException {
        if(task.isSuccessful()) {
            return task.getResult();
        } else {
            throw new ExecutionException(task.getException());
        }
    }

    public static TaskAlliance allianceTask(Task... tasks)
    {
        return new TaskAlliance(tasks);
    }

    public static TaskAlliance allianceTask(Collection<? extends Task<?>> taskCollection)
    {
        return new TaskAlliance(taskCollection);
    }

    public static TaskAlliance allianceTask(Task task)
    {
        return new TaskAlliance(task);
    }

    public static <PResult> Task<PResult> execute(final TaskSource<PResult> taskSource)
    {
        return execute(getDefaultExecutor(), taskSource);
    }

    public static <PResult> Task<PResult> execute(Executor executor, final TaskSource<PResult> taskSource)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    taskSource.setResult(taskSource.call());
                } catch (Exception e) {
                    taskSource.setException(e);
                }
            }
        });
        return taskSource.getTask();
    }

    private static class WaiterTask implements MainListenerService {
        private final CountDownLatch zztj;

        private WaiterTask() {
            this.zztj = new CountDownLatch(1);
        }

        public void await() throws InterruptedException {
            this.zztj.await();
        }

        public boolean await(long var1, TimeUnit var3) throws InterruptedException {
            return this.zztj.await(var1, var3);
        }

        @Override
        public void OnFailure(Exception e) {
            this.zztj.countDown();
        }

        @Override
        public void OnSuccess(Object o) {
            this.zztj.countDown();
        }
    }

    interface MainListenerService extends OnFailureListener, OnSuccessListener {
    }
}
