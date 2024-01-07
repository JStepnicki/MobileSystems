package SandZ.Tutors.data.classes;

import java.io.Serializable;
import java.util.List;

public class TeacherClass implements Serializable {
    private String id;
    private String email;
    private String name;
    private String surname;
    private List<String> subjects;
    private int price;
    private final List<Integer> rates;

    public TeacherClass(String id, String email, String name, String surname, List<String> subjects, List<Integer> rates, int price) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.subjects = subjects;
        this.rates = rates;
        this.price = price;
    }
    public String getRate(){
        String rateString = "-";
        if(rates.size() == 0)
            return rateString;
        float sum = 0;
        for(int i = 0; i < rates.size(); i++){
            sum += rates.get(i);
        }
        float rate = sum/rates.size();
        rateString = Float.toString(rate);
        return rateString;
    }

    public String getStringPrice() {
        return Integer.toString(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Integer> getRates() {
        return rates;
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


    public float getRating(){
        float sum = 0;
        for(int i = 0; i < rates.size(); i++){
            sum += rates.get(i);
        }
        if(sum == 0)
            return 0;
        float rate = sum/rates.size();
        return rate;
    }
}
