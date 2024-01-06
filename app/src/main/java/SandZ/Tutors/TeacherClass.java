package SandZ.Tutors;

import java.util.List;

public class TeacherClass {
    private String id;
    private String email;
    private String name;
    private String surname;
    private List<String> subjects;
    private List<Integer> rates;

    public TeacherClass(String id, String email, String name, String surname, List<String> subjects, List<Integer> rates) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.subjects = subjects;
        this.rates = rates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }
}
