import java.util.Stack;

public abstract class Issue {
    protected long ID, priority;
    protected String title, description, creator, assignee, tag, status;
    protected String timestamp;
    protected Stack<Comment> comment_stack;
    public boolean addComment;
}
