import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

public class Issues extends Issue{
    protected static long number=1;
    protected long ID, priority;
    protected String title, description, creator, assignee, tag, status;
    protected String timestamp;
    protected Stack<Comments> CommentStack;

    public Issues(long ID,String title, String tag, long priority, String assignee, String creator, String description, String status,String timestamp) {
        this.ID = ID;
        this.title = title;
        this.tag = tag;
        this.priority = priority;
        this.assignee = assignee;
        this.creator = creator;
        this.description = description;
        this.status = status;
        this.CommentStack=new Stack<Comments>();
        this.timestamp=timestamp;
        number++;
    }
    public Issues(String title, String tag, long priority, String assignee, String creator, String description) {
        this.ID = number;
        this.title = title;
        this.tag = tag;
        this.priority = priority;
        this.assignee = assignee;
        this.creator = creator;
        this.description = description;
        this.status = "Open";
        this.CommentStack=new Stack<Comments>();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.timestamp=(myDateObj.format(myFormatObj));
        number++;
    }

    public long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Stack<Comments> getComment() {
        return CommentStack;
    }

    public void setComment(Stack<Comments> comment) { this.CommentStack = comment;}

    public void addComment(Comments e){
        this.CommentStack.add(e);
    }

    @Override
    public String toString() {
        return "Issues{ " +"\n"+
                "ID = " + ID +"\n"+
                "priority = " + priority +"\n"+
                "title = " + title  +"\n"+
                "description = " + description   +"\n"+
                "creator = " + creator  +"\n"+
                "assignee = " + assignee  +"\n"+
                "tag = " + tag  +"\n"+
                "status = "  + status  +"\n"+
                "timestamp = " + timestamp +"\n"+
                "CommentStack = " + CommentStack +"\n"+
                '}'+"\n";
    }
}
