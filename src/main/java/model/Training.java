package model;

import annatations.Exclude;

import java.util.Date;
import java.util.Objects;

public class Training {
    private int id;
    private int numberGym;
    private Trainer trainer;
    @Exclude
    private Apprentice apprentice;
    private Date date;

    public Training() {
    }

    public Training(int id, int numberGym, Trainer trainer, Apprentice apprentice, Date date) {
        this.id = id;
        this.numberGym = numberGym;
        this.trainer = trainer;
        this.apprentice = apprentice;
        this.date = date;
    }

    public Training(int numberGym, Trainer trainer, Apprentice apprentice, Date date) {
        this.numberGym = numberGym;
        this.trainer = trainer;
        this.apprentice = apprentice;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberGym() {
        return numberGym;
    }

    public void setNumberGym(int numberGym) {
        this.numberGym = numberGym;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Apprentice getApprentice() {
        return apprentice;
    }

    public void setApprentice(Apprentice apprentice) {
        this.apprentice = apprentice;
    }

    public Date     getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id &&
                numberGym == training.numberGym &&
                Objects.equals(trainer, training.trainer) &&
                Objects.equals(apprentice, training.apprentice) &&
                Objects.equals(date, training.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberGym, trainer, apprentice, date);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", numberGym=" + numberGym +
                ", trainer=" + trainer +
                ", date=" + date +
                '}';
    }
}
