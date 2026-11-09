// History record

import java.time.LocalDateTime;

public class Record {
    String link;
    String date;
    StringBuilder str = new StringBuilder();

    Record(String link) {
        this.link = link;
        this.date = LocalDateTime.now().toString();
        str.append(link).append("     ").append(date);

    }

    Record(String link, String date) {
        this.link = link;
        this.date = date;
    }


    @Override
    public String toString() {
        return link + "   " + date;
    }

}
