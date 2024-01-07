import java.util.Stack;

public class Projects {
    static int number = 1;
    long id;
    String project_names;
    Stack<Issues> issuesStack;

    public Projects(long id, String project_names) {
        this.id = number;
        this.project_names = project_names;
        this.issuesStack = new Stack<Issues>();
        number++;
    }

    public Projects(long id, String project_names,Stack<Issues> issuestack ) {
        this.id = number;
        this.project_names = project_names;
        this.issuesStack = issuestack;
        number++;
    }
    public void pushIssues(Issues x){
        issuesStack.push(x);
    }
    public void popIssues(){
        issuesStack.pop();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProject_names() {
        return project_names;
    }

    public void setProject_names(String project_names) {
        this.project_names = project_names;
    }

    public Stack<Issues> getIssuesStack() {
        return issuesStack;
    }

    public void setIssuesStack(Stack<Issues> issuesStack) {
        this.issuesStack = issuesStack;
    }
    public void addIssues(Issues a){
        issuesStack.add(a);
    }
    @Override
    public String toString() {
        return "Projects{" +"\n"+
                "id =" + id +"\n"+
                "project_names =" + project_names + '\'' +"\n"+
                "issuesStack = "+"\n" + issuesStack +"\n"+
                '}'+"\n";
    }
}
