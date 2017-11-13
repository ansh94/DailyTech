package com.anshdeep.dailytech.util.rx;

import io.reactivex.Scheduler;

/**
 * Created by ansh on 20/10/17.
 */

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
