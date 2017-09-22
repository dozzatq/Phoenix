package com.github.dozzatq.phoenix.notification;

import android.support.annotation.NonNull;

/**
 * Created by Rodion Bartoshik on 06.12.2016.
 */

public interface PhoenixNotification {
    public void didReceiveNotification(@NonNull String notification, Object... values);
}
