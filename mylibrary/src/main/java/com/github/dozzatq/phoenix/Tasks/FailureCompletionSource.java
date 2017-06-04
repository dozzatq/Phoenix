package com.github.dozzatq.phoenix.Tasks;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by dxfb on 04.06.2017.
 */

class FailureCompletionSource<PResult> implements OnTaskCompleteListener<PResult> {

    private Executor executor;
    private final Object waitObject=new Object();
    private OnFailureListener pResultFailureListener;

    public FailureCompletionSource(Executor executor, OnFailureListener pResultFailureListener) {
        this.executor = executor;
        this.pResultFailureListener = pResultFailureListener;
    }

    @Override
    public void OnTaskComplete(@NonNull final Task<PResult> pResultTask) {
        synchronized (waitObject)
        {
            if (executor==null || pResultFailureListener==null)
                throw new NullPointerException("Executor & OnFailureListener must not be null!");

            if (pResultTask.isExcepted())
            {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        pResultFailureListener.OnFailure(pResultTask.getException());
                    }
                });
            }
        }
    }
}
