public class Reaction {
    String reaction;
    long count;

    public Reaction(String reaction) {
        this.reaction = reaction;
        this.count = 0;
    }
    public Reaction(String reaction, long count) {
        this.reaction = reaction;
        this.count = count;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void displayReactionList(){
        System.out.println(count+" persons react with "+reaction);
    }
    @Override
    public String toString() {
        return "Reaction{"+"\n" +
                "reaction = " + reaction +"\n"+
                "count = " + count +"\n"+
                '}'+"\n";
    }
}