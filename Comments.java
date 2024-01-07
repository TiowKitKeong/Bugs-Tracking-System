import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Comments extends Comment {
    protected static long count=0;
    protected long ID;
    protected String text, comment_user;
    protected String timestamp;
    ArrayList<Reaction> ReactionList;

    public Comments(String comment_user, String text) {
        this.ID = ++count;
        this.text = text;
        this.comment_user = comment_user;
        ReactionList=new ArrayList<>();
        ReactionList.add(new Reaction("happy"));ReactionList.add(new Reaction("thumbsUp"));ReactionList.add(new Reaction("angry"));
        ReactionList.add(new Reaction("sad"));ReactionList.add(new Reaction("like"));
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        timestamp=(myDateObj.format(myFormatObj));
    }
    public Comments(long ID, String comment_user, String text, String timestamp) {
        this.ID =ID;
        this.text = text;
        this.comment_user = comment_user;
        this.ReactionList=new ArrayList<>();
        ReactionList.add(new Reaction("happy"));ReactionList.add(new Reaction("thumbsUp"));ReactionList.add(new Reaction("angry"));
        ReactionList.add(new Reaction("sad"));ReactionList.add(new Reaction("like"));
        this.timestamp=timestamp;
        count++;
    }

    public boolean addReaction(String ReactType){
        for(int i=0;i<ReactionList.size();i++){
            String react = ReactionList.get(i).reaction;
            if(react.equalsIgnoreCase(ReactType)){
               ReactionList.get(i).count++;
               return true;
            }
        }
            return false;
    }

    @Override
    public String toString() {
        return "comments{" +"\n"+
                "ID = " + ID +"\n"+
                "text = " + text + '\'' +"\n"+
                "comment_user = " + comment_user + '\'' +"\n"+
                "timestamp = " + timestamp + '\'' +"\n"+
                "ReactionList = " +"\n"+ ReactionList +"\n"+
                '}'+"\n";
    }
}