package com.example.user.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by User on 8/7/2017.
 */

public class JNotificationjobservice extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
