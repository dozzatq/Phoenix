package com.github.dozzatq.phoenix.Notification;

import java.util.concurrent.Executor;

/**
 * Created by dxfb on 04.06.2017.
 */

class CenterQueue extends DefaultCenterQueue {

    public CenterQueue(Executor queueExecutor) {
        super(queueExecutor);
    }
}
