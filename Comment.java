import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class Comment {
    static int number=1;
    protected int ID;
    protected String text, comment_user;
    protected String timestamp;
    protected ArrayList<Reaction> reaction;


    public boolean addReaction;


}
