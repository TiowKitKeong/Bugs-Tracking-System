import jdk.swing.interop.SwingInterOpUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CreateObjectFromJson {
    ArrayList<Projects> project_list = new ArrayList<Projects>();
    public void json(){
        try {
            JSONParser parser = new JSONParser();
            FileReader file = new FileReader("data.json");
            Object obj = parser.parse(file);
            JSONObject jsonobject0 = (JSONObject) obj;
            JSONArray projectsArray = (JSONArray) jsonobject0.get("projects");
            int s = projectsArray.size();
            //System.out.println(projectsArray.size());
            for(int i=0;i<s;i++){
                JSONObject project_object =(JSONObject) projectsArray.get(i);
                ReadAndCreateProject(project_object);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void ReadAndCreateProject(JSONObject a) {
        JSONObject prj_obj = a;
        long id = (long) a.get("id");
        String project_names = (String) prj_obj.get("name");
        Projects p = new Projects(id,project_names);
        JSONArray issuesarr = (JSONArray) prj_obj.get("issues");
        int size = issuesarr.size(); //number of issues in current project
        for(int i=0;i<size;i++){
            JSONObject issues_obj =(JSONObject) issuesarr.get(i); //reading all issues obj in curr prject
            ReadAndCreateIssues(p,issues_obj);
        }
        project_list.add(p);
    }

    public void ReadAndCreateIssues(Projects o,JSONObject a) {
        JSONObject issue = a;
        long issueID = (long) issue.get("id"); //issue id
        String Status = (String) issue.get("status"); //status
        JSONArray TagArray = (JSONArray) issue.get("tag"); //tag array
        String tag = readTagArray(TagArray);//get the tag inside array
        //System.out.println(tag);
        long priority = (long) issue.get("priority"); //priority
        String timestamp =(String) issue.get("timestamp"); //timestamp->is a string
        String title = (String) issue.get("title"); //title
        String assignee = (String) issue.get("assignee"); //assigned to
        String creator = (String) issue.get("createdBy"); //creator
        String description = (String) issue.get("descriptionText"); //issue description
        Issues x = new Issues(issueID,title,tag,priority,assignee,creator,description,Status,timestamp);//////////////
        JSONArray comment_arr =(JSONArray) issue.get("comments");
        int commentarr_size = comment_arr.size();
        for(int i=0;i<commentarr_size;i++) {
            JSONObject comment_obj = (JSONObject)comment_arr.get(i);
            ReadAndCreateComments(x, comment_obj);
        }
        o.issuesStack.add(x);
    }

    public void ReadAndCreateComments(Issues x,JSONObject comment_obj){
        String text = (String)comment_obj.get("text");
        long commentid = (long) comment_obj.get("comment_id");
        String user = (String) comment_obj.get("user");
        String timestamp=(String) comment_obj.get("timestamp");
        Comments cm = new Comments(commentid,user,text,timestamp);
        JSONArray React_Arr = (JSONArray) comment_obj.get("react");
        int reactarr_size=React_Arr.size();
        for(int i=0;i<reactarr_size;i++){
            JSONObject reaction_obj = (JSONObject) React_Arr.get(i);
            ReadAndCreateReaction(cm,reaction_obj);
        }
        x.CommentStack.add(cm);
    }

    public String readTagArray(JSONArray arr){
        JSONArray raw = (JSONArray) arr;
        int size = raw.size();
        String tag="";
        for(int i=0;i<size;i++){
            String o =(String) raw.get(i); //get the tag object
            if(i==size-1){tag+=o;}
            else{ tag = tag + o + ", " ; }
        }
        return tag;
    }

    public void ReadAndCreateReaction(Comments cm,JSONObject reaction_obj){
        JSONObject react_obj=reaction_obj;
        String reaction = (String) react_obj.get("reaction");
        long count = (long) react_obj.get("count");
        Reaction z = new Reaction(reaction,count);
        cm.ReactionList.add(z);
    }


    public ArrayList<Projects> getProject_list() {
        return project_list;
    }
}

