package model;

import annatations.Exclude;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TrainerSchedule {
    private HashMap<DayOfWeek, Time[]> schedule;

    @Exclude
    private Trainer trainer;

    public TrainerSchedule() {
        this.schedule = new HashMap<>();
    }

    public void setWorkDay(DayOfWeek dayOfWeek, Time start, Time finish){
        if (start != null){
            if (finish == null)
                finish = new Time(759600000);
            this.schedule.put(dayOfWeek, new Time[]{start, finish});
        }
    }

    public void setWorkDay(DayOfWeek dayOfWeek, Time start){
        this.schedule.put(dayOfWeek, new Time[]{start, new Time(759600000)});
    }

    public Time[] getScheduleOfTheDay(DayOfWeek dayOfWeek){
        Time[] times = this.schedule.get(dayOfWeek);
        return times != null ? times : new Time[2];
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    @Override
    public String toString() {
        return this.schedule.entrySet().stream().map(o -> o.getKey().toString() + o.getValue()[0] + o.getValue()[1]).collect(Collectors.joining(" "));
    }

}
