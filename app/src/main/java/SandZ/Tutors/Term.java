package SandZ.Tutors;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Term {
    private Timestamp timestamp;
    private boolean isBooked;

    public Term(Timestamp timestamp, boolean isBooked) {
        this.timestamp = timestamp;
        this.isBooked = isBooked;
    }

    public String getDateString() {
        return timestamp.toDate().toString();
    }

    public long getTime() {
        return timestamp.toDate().getTime();
    }

    public boolean checkDate(int year, int month, int day) {
        int termYear = timestamp.toDate().getYear() + 1900;
        int termMonth = timestamp.toDate().getMonth();
        int termDay = timestamp.toDate().getDay();
        return termYear == year && termMonth == month && termDay == day;
    }

    public String getTimeAsString() {
        // Utwórz kalendarz i ustaw go na czas obiektu Timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.toDate().getTime());

        // Dodaj godzinę do kalendarza
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        // Utwórz obiekt Date na podstawie kalendarza
        Date modifiedDate = calendar.getTime();

        // Ustaw strefę czasową na UTC+1 dla SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+1"));

        return sdf.format(modifiedDate);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
