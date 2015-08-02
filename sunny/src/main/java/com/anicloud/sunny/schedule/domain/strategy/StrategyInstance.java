package com.anicloud.sunny.schedule.domain.strategy;

import com.anicloud.sunny.schedule.domain.schedule.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangbin on 7/18/15.
 */
public class StrategyInstance implements Schedulable, ScheduleStateListener, Serializable {
    private static final long serialVersionUID = 2408334140270041423L;

    public String strategyId;
    public ScheduleState state;
    public Integer stage;
    public List<FeatureInstance> featureInstanceList;

    public StrategyAction action;
    public Long timeStamp;

    public boolean isScheduled;
    transient public ScheduleStateListener listener;

    public StrategyInstance() {
    }

    public StrategyInstance(String strategyId, ScheduleState state,
                            Integer stage, List<FeatureInstance> featureInstanceList,
                            StrategyAction action,
                            Long timeStamp) {
        this.strategyId = strategyId;
        this.state = state;
        this.stage = stage;
        this.featureInstanceList = featureInstanceList;
        this.action = action;
        this.timeStamp = timeStamp;
        this.isScheduled = false;
    }

    public void  prepareSchedule(ScheduleManager scheduleManager, ScheduleStateListener listener, String hashUserId) {
        for (int i=0; i<featureInstanceList.size(); i++) {
            FeatureInstance featureInstance = featureInstanceList.get(i);
            featureInstance.scheduleManager = scheduleManager;

            Set<ScheduleTrigger> scheduleTriggerList = new HashSet<>();
            for (int j=0; j<featureInstance.triggerInstanceList.size(); j++) {
                TriggerInstance triggerInstance = featureInstance.triggerInstanceList.get(j);
                ScheduleTrigger scheduleTrigger = new ScheduleTrigger(
                        strategyId + i,
                        strategyId,
                        strategyId + i + j,
                        strategyId + i,
                        triggerInstance.startTime,
                        triggerInstance.repeatInterval,
                        triggerInstance.repeatCount);
                scheduleTriggerList.add(scheduleTrigger);
            }
            ScheduleJob scheduleJob = new ScheduleJob(
                    strategyId + i,
                    strategyId,
                    ScheduleState.NONE,
                    "",
                    ScheduleJobEntry.class,
                    scheduleTriggerList,
                    featureInstance,
                    featureInstance.isScheduleNow);

            featureInstance.hashUserId = hashUserId;
            featureInstance.scheduleJob = scheduleJob;
            featureInstance.listener = this;
        }
        this.listener = listener;
        this.isScheduled = true;
    }

    @Override
    public boolean start() {
        if (!this.isScheduled) {
            return false;
        }
        switch (state) {
            case NONE:
                if (stage < featureInstanceList.size()) {
                    state = ScheduleState.RUNNING;
                    listener.onScheduleStateChanged(this, state);
                    featureInstanceList.get(stage).start();
                } else {
                    state = ScheduleState.DONE;
                    listener.onScheduleStateChanged(this, state);
                }
                return true;
            case RUNNING:
                break;
            case SUSPENDED:
                break;
            case DONE:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean stop() {
        if (!this.isScheduled) {
            return false;
        }
        switch (state) {
            case NONE:
                break;
            case RUNNING:
            case SUSPENDED:
                featureInstanceList.get(stage).stop();
                state = ScheduleState.DONE;
                listener.onScheduleStateChanged(this, state);
                return true;
            case DONE:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean pause() {
        if (!this.isScheduled) {
            return false;
        }
        switch (state) {
            case NONE:
                featureInstanceList.get(stage).pause();
                state = ScheduleState.SUSPENDED;
                listener.onScheduleStateChanged(this, state);
                return true;
            case RUNNING:
                break;
            case SUSPENDED:
                break;
            case DONE:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean resume() {
        if (!this.isScheduled) {
            return false;
        }
        switch (state) {
            case NONE:
                break;
            case RUNNING:
                break;
            case SUSPENDED:
                featureInstanceList.get(stage).resume();
                state = ScheduleState.RUNNING;
                listener.onScheduleStateChanged(this, state);
                return true;
            case DONE:
                break;
            default:
                break;
        }
        return false;
    }

    private void next() {
        switch (state) {
            case NONE:
                break;
            case RUNNING:
//                add the next job
                stage += 1;
                state = ScheduleState.NONE;
                start();
                break;
            case SUSPENDED:
                break;
            case DONE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onScheduleStateChanged(Object src, ScheduleState state) {
        if (state == ScheduleState.DONE) {
            next();
        }
    }
}
