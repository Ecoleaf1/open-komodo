package net.wigoftime.open_komodo.objects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class MailWrapper {
    public final UUID sender;
    public final LocalDateTime date;
    public final String message;

    public MailWrapper (UUID sender, LocalDateTime date, String message) {
        this.sender = sender;
        this.date = date;
        this.message = message;
    }
}
