package model;

import java.util.Objects;

public class Trainer {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private int experience;
    private TrainerSchedule trainerSchedule;

    public Trainer() {
    }

    public Trainer(String surname, String name, String patronymic, int experience) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.experience = experience;
    }

    public Trainer(int id, String surname, String name, String patronymic, int experience) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.experience = experience;
    }

    public TrainerSchedule getTrainerSchedule() {
        return trainerSchedule;
    }

    public void setTrainerSchedule(TrainerSchedule trainerSchedule) {
        this.trainerSchedule = trainerSchedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return id == trainer.id &&
                experience == trainer.experience &&
                Objects.equals(surname, trainer.surname) &&
                Objects.equals(name, trainer.name) &&
                Objects.equals(patronymic, trainer.patronymic) &&
                Objects.equals(trainerSchedule, trainer.trainerSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, experience, trainerSchedule);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", experience=" + experience +
                ", trainerSchedule=" + trainerSchedule +
                '}';
    }
}
