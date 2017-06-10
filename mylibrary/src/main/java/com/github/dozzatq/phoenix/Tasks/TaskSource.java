package com.github.dozzatq.phoenix.Tasks;

/**
 * Created by rodeon on 5/26/17.
 */

public abstract class TaskSource<PResult> extends TaskCompletionSource<PResult>{
    protected Task<PResult> task = new Task<PResult>();

    @Override
    public final Task<PResult> getTask() {
        return task;
    }

    @Override
    public final String getTag()
    {
        return "TaskSource";
    }
}
