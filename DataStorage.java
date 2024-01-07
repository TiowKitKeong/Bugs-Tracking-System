import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class DataStorage {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLACK = "\u001B[30m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";
    String current_username;
    ArrayList<Projects> project_list = new ArrayList<>();
    ArrayList<user> user_list = new ArrayList<>(); //a list of user
    JSONObject jsonobject0 = new JSONObject(); //initial object
    JSONArray ReactArr = new JSONArray(); //each project Reaction Array
    JSONArray userarray = new JSONArray();
    JSONArray projectsarray = new JSONArray(); //projectsarray
    int SelectedProjectId = -1;
    JSONObject SelectedProject = new JSONObject();//Selected Project by user
    JSONArray IssuesArrayForSelectedProject = new JSONArray(); //Issues Array For selected project
    int SelectedIssueId = -1;
    JSONObject SelectedIssue = new JSONObject(); //Selected Issue
    int SelectedCommentId = -1;
    JSONObject CommentObject = new JSONObject(); //comment object in the comment array in selected issue
    JSONArray ReactionArrayInSelectedComment = new JSONArray(); // Reaction Array in selected comment
    int SelectedReactionType = -1;

    public void ProjectDashboardJson() {
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        project_list = b.getProject_list();
        //System.out.println(project_list);
        current_username = UserInterface.currentuser;
        try {
            JSONParser parser = new JSONParser();
            FileReader file = new FileReader("data.json");
            Object obj = parser.parse(file);
            jsonobject0 = (JSONObject) obj;
            projectsarray = (JSONArray) jsonobject0.get("projects");
            //System.out.println(arr.size());
            System.out.println();
            System.out.println(BLUE+"PROJECT DASHBOARD");
            System.out.println("------------------");
            System.out.println("+-------+----------------+--------+");
            System.out.printf("|  %-5s%-2s%-15s%-2s%-5s |\n","ID","| ","Project Names","|","Issues");
            System.out.println("+-------+----------------+--------+");
            projectsarray.forEach(a -> ReadProjectDetailsJson((JSONObject) a));
            IssueDashboard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void IssueDashboard() {
        CreateObjectFromJson COFJ1 = new CreateObjectFromJson();
        COFJ1.json();// existing
        ArrayList<Projects> raw = COFJ1.project_list; //obtain project list from json file
        //System.out.println(project_list);
        boolean status = false;
        boolean status2 = false;
        JSONParser parser = new JSONParser();
        while (status == false) {
            try {
                FileReader file = new FileReader("data.json");
                Object obj = parser.parse(file);
                jsonobject0 = (JSONObject) obj; //object obtained from file
                projectsarray = (JSONArray) jsonobject0.get("projects");
                Scanner sc = new Scanner(System.in);
                System.out.println(RESET+"Enter selected project id to check project :");
                String command = sc.nextLine();
                boolean ProjectExist = false;
                ProjectExist = (ProjectIdValid(command, projectsarray));
                if (ProjectExist == true) {
                    SelectedProject = (JSONObject) projectsarray.get(Integer.parseInt(command) - 1);//get the selected project
                    SelectedProjectId = Integer.parseInt(command) - 1;
                    System.out.println();
                    System.out.println(RED+"ISSUE DASHBOARD ");
                    System.out.println("--------------- ");
                    System.out.println("+----+-----------------------------------+---------------+----------------------+---------------+---------------------+-------------+------------+");
                    System.out.printf("| %-3s%-10s%-26s%-5s%-11s%-8s%-15s%-4s%-12s%-8s%-14s%-2s%-11s%-3s%-10s%-1s\n", "ID","|","Title","|","Status","|","Tag","|","Priority","|","Time","|  ","Assignee","|","CreatedBy","|");
                    System.out.println("+----+-----------------------------------+---------------+----------------------+---------------+---------------------+-------------+------------+");
                    System.out.print(RESET);
                    status = DisplayIssuesListJson(command);
                    JSONObject project = (JSONObject) projectsarray.get(Integer.parseInt(command) - 1);
                    JSONArray issuesarray = (JSONArray) project.get("issues");
                    IssuesArrayForSelectedProject = (JSONArray) SelectedProject.get("issues"); //issue array in selected project
                    while (status2 == false) {
                        Scanner sc2 = new Scanner(System.in);
                        System.out.println("Enter selected issues id to check issues :");
                        System.out.println("or 's' to search");
                        System.out.println("or 'c' to create issues");
                        String cmmnd = sc2.nextLine();
                        boolean s = false;
                        int searchresult = -1;
                        if (cmmnd.charAt(0) == 's') {
                            while (s != true) {
                                Scanner scanner3 = new Scanner(System.in);
                                System.out.println("Enter Your Title to search : ");
                                String input = scanner3.nextLine();
                                searchresult = search(IssuesArrayForSelectedProject, input);
                                if (searchresult >= 0) {
                                    s = true;
                                    cmmnd = "" + searchresult;
                                } else {
                                    System.out.println("The title of issues you are searching does not exist. Pls Try Again!");
                                }
                            }
                        }//just a mock for searching
                        Stack<Issues> ori = new Stack<>();
                        Stack<Issues> temp = new Stack<>();
                        if (cmmnd.charAt(0) == 'c') {
                            status2 = true;
                            boolean UR = true;
                            do {
                                String username = UserInterface.currentuser;
                                System.out.println();
                                System.out.println("Creating Issue.....");
                                Thread.sleep(1500);
                                Scanner scaner = new Scanner(System.in);
                                System.out.println("\nEnter your issue title: ");
                                String issuetitle = scaner.nextLine();
                                System.out.println("Enter your issue tag [Frontend/Backend/cmty:bug-report] : ");
                                String issuetag = scaner.nextLine();
                                String priority;
                                while (true) {
                                    System.out.println("Enter your priority number (1-9): ");
                                    priority = scaner.nextLine();
                                    if (Integer.parseInt(priority) > 0 || Integer.parseInt(priority) < 10)
                                        break;
                                    else {
                                        System.out.println("Input Out Of Range");
                                        continue;
                                    }
                                }
                                System.out.println("Enter your issue description: ");
                                String issuedescription = scaner.nextLine();
                                LocalDateTime myDateObj = LocalDateTime.now();
                                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                                String time = myDateObj.format(myFormatObj);
                                System.out.println("Enter your assignee (\"-\" if no): ");
                                String assignee = scaner.nextLine();
                                Issues newissu = new Issues(issuesarray.size() + 1, issuetitle, issuetag,
                                        Long.parseLong(priority), assignee, username, issuedescription, "Open", time);// new create
                                ori.push(newissu);

                                action:
                                {
                                    boolean UR2 = true;
                                    while (UR2 == true) {
                                        System.out.println("\nEnter 'u' to undo issue, 'r' to redo issue, 'i' to write another issue, 'x' to continue");
                                        command = sc.nextLine();
                                        if (command.charAt(0) == 'u') {
                                            temp.push(ori.pop());
                                            System.out.println(ori);
                                            System.out.println("Issues created: " + ori.size());
                                        } else if (command.charAt(0) == 'r') {
                                            ori.push(temp.pop());
                                            System.out.println(ori);
                                            System.out.println("Issues created: " + ori.size());
                                        } else if (command.charAt(0) == 'i')
                                            break action;
                                        else if (command.charAt(0) == 'x') {
                                            if (ori.size() == 0) {
                                                System.out.println();
                                                System.out.println("No issues created");
                                                System.out.println();
                                            } else {
                                                for (int i = 0; i < ori.size(); i++) {
                                                    FileWriter writernewissue = new FileWriter("data.json");
                                                    JSONObject newissue = new JSONObject();
                                                    newissue.put("id", issuesarray.size() + 1);
                                                    newissue.put("title", ori.get(i).title);
                                                    newissue.put("status", "Open");
                                                    JSONArray issuetagarray = new JSONArray();
                                                    issuetagarray.add(ori.get(i).tag);
                                                    newissue.put("tag", issuetagarray);
                                                    newissue.put("descriptionText", ori.get(i).description);
                                                    newissue.put("priority", ori.get(i).priority);
                                                    newissue.put("timestamp", ori.get(i).timestamp);
                                                    newissue.put("assignee", ori.get(i).assignee);
                                                    newissue.put("createdBy", ori.get(i).creator);
                                                    JSONArray comments = new JSONArray();
                                                    newissue.put("comments", comments);
                                                    issuesarray.add(newissue);
                                                    writernewissue.write(jsonobject0.toString());
                                                    writernewissue.flush();
                                                    writernewissue.close();
                                                }
                                                UR = false;
                                                System.out.println();
                                                System.out.println("Issue is successfully created !!!");
                                                System.out.println();
                                                break action;
                                            }
                                            UR = false;
                                            UR2 = false;
                                        } else {
                                            System.out.println("Invalid input");
                                            continue;
                                        }
                                    }
                                }
                            } while (UR == true);
                            System.out.println();
                            System.out.println("GOING BACK TO PROJECT DASHBOARD...");
                            System.out.println();
                            Thread.sleep(1200);
                            ProjectDashboardJson();
                        }//just a mock for creating issue

                        if ((cmmnd.charAt(0) != 's' && cmmnd.charAt(0) != 'c')) {
                            if (IssueIdValid(cmmnd) == true) {
                                int index = Integer.parseInt(cmmnd) - 1;
                                if (s == true) {
                                    index = searchresult;
                                }
                                try {
                                    status2 = true;
                                    SelectedIssueId = index;
                                    SelectedIssue = (JSONObject) IssuesArrayForSelectedProject.get(index); //selected issue
                                    DisplaySelectedIssue(SelectedIssue);
                                } catch (IndexOutOfBoundsException e) {
                                    System.err.println("Invalid Input");
                                    status2 = false;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ReadProjectDetailsJson(JSONObject a) {
        JSONObject raw = a;
        long id = (long) a.get("id");
        String project_names = (String) a.get("name");
        JSONArray temp = (JSONArray) a.get("issues");
        int number = temp.size();
        System.out.printf("|  %-5s%-2s%-15s%-4s%-5s|\n", id,"|", project_names,"|   ", number);
        System.out.println("+-------+----------------+--------+");

    }

    public boolean ProjectIdValid(String id, JSONArray projectsarray) {
        try {
            int i = Integer.parseInt(id);
            if (i >= 10) {
                System.err.println("invalid input");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input");
            return false;
        }
        JSONObject project = new JSONObject();
        try {
            project = (JSONObject) projectsarray.get(Integer.parseInt(id) - 1);//get the selected project
        } catch (IndexOutOfBoundsException e) {
            System.err.println("invalid input");
            return false;
        }
        return true;
    }

    public boolean IssueIdValid(String id) {
        try {
            int i = Integer.parseInt(id);
            if (i >= 10) {
                System.err.println("invalid input");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input");
            return false;
        }
        return true;
    }

    public boolean DisplaySelectedIssue(JSONObject a) throws java.text.ParseException, FileNotFoundException, InterruptedException {
        JSONObject raw = a; //issue object passed in
        long issueID = (long) raw.get("id"); //issue id
        String Status = (String) raw.get("status"); //status
        JSONArray TagArray = (JSONArray) raw.get("tag"); //tag array
        String tag = readTagArray(TagArray);//get the tag inside array
        //System.out.println(tag);
        long priority = (long) raw.get("priority"); //priority
        String timestamp = (String) raw.get("timestamp"); //timestamp->is a string
        String title = (String) raw.get("title"); //title
        String assignee = (String) raw.get("assignee"); //assigned to
        String creator = (String) raw.get("createdBy"); //creator
        String description = (String) raw.get("descriptionText"); //issue description
        //System.out.println(description);
        System.out.println();
        System.out.println(CYAN+"Issue ID: " + issueID + "      " + "Status: " + Status);
        System.out.println("Tag: " + tag + "      " + "Priority: " + priority + "     " + "Created On: " + timestamp);
        System.out.println("Title : " + title);
        System.out.println("Assigned to: " + assignee + "                   " + "Created By: " + creator);
        System.out.println(YELLOW);
        System.out.println("Issue Description");
        System.out.println("-----------------");
        System.out.print(description + "\n");
        System.out.println(RESET);
        System.out.println(PURPLE+"Comments");
        System.out.println("---------");
        JSONArray commentArr = (JSONArray) raw.get("comments");//get comment array
        if (commentArr.size() == 0)
            System.out.println("No comments\n");
        else
            commentArr.forEach(obj -> readCommentArray((JSONObject) obj));
            System.out.print(RESET);
        boolean status = false;
        while (status == false) {
            Scanner s = new Scanner(System.in);
            System.out.println(RESET+"Enter");
            System.out.println("'r' to react ");
            System.out.println("or 'c' to comment ");
            System.out.println("or 'help' for more commands: ");
            String command = s.nextLine();
            boolean valid = valid(command); //check validity of command
            if (valid == false) {  //if command is not valid then prompt user to input again
                status = valid;
            }
            if (valid == true) {  //if command is valid->operation
                status = operation(command, commentArr);
            }
        }
        return true;
    }

    public String readTagArray(JSONArray arr) {
        JSONArray raw = arr;
        int size = raw.size();
        String tag = "";
        for (int i = 0; i < size; i++) {
            String o = (String) raw.get(i); //get the tag object
            if (i == size - 1) {
                tag += o;
            } else {
                tag = tag + o + ", ";
            }
        }
        return tag;
    }

    public void readCommentArray(JSONObject obj) {
        JSONObject comment = obj; //comment object
        ReactArr = (JSONArray) comment.get("react");
        String reaction = "";
        String text = (String) comment.get("text");
        long commentid = (long) comment.get("comment_id");
        String user = (String) comment.get("user");
        String timestamp = (String) comment.get("timestamp");
        System.out.print("#" + commentid);
        System.out.print("    Created on: " + timestamp);
        System.out.print("    By: " + user + "\n");
        System.out.print(text + "\n");
        System.out.print("$$ ");
        ReactArr.forEach(a -> readReactionArray((JSONObject) a));
        System.out.println();
        System.out.println();
    }

    public void readReactionArray(JSONObject obj) {
        JSONObject raw = obj;
        String reaction = (String) raw.get("reaction");
        long count = (long) raw.get("count");
        System.out.print(count + " people react with " + reaction + "  ");
    }

    //NEW
    public boolean operation(String c, JSONArray commentArr) throws FileNotFoundException, InterruptedException {
        if (c.length() == 1) {
            if (c.charAt(0) == 'r') {
                invokeAddReaction(commentArr);
                return true;
            }
            if (c.charAt(0) == 'h') {
                MainScreen m1 = new MainScreen();
                m1.show();
                MainScreen m2 = new MainScreen();
                m2.show();
                ProjectDashboardJson();
            }
            if (c.charAt(0) == 'z') {
                GenerateReport();
                System.out.println("GOING BACK TO PROJECT DASHBOARD AFTER GENERATING REPORT....");
                System.out.println();
                System.out.println();
                Thread.sleep(1200);
                ProjectDashboardJson();
            }

            if (c.charAt(0) == 'c') {
                boolean UR = true;
                Stack<Comments> ori = new Stack<>();
                Stack<Comments> temp = new Stack<>();
                do {
                    String username = UserInterface.currentuser;
                    Scanner sc = new Scanner(System.in);
                    System.out.println("\nCreating Comment....");
                    System.out.println("\nEnter your text: ");
                    String commenttext = sc.nextLine();
                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    String time = myDateObj.format(myFormatObj);
                    Comments newcomm = new Comments(ori.size() + 1, username, commenttext, time);// new create
                    ori.push(newcomm);

                    action:
                    {
                        boolean UR2 = true;
                        while (UR2 == true) {
                            System.out.println("\nEnter 'u' to undo comment, 'r' to redo comment, 'i' to write another comment, 'x' to continue");
                            String command = sc.nextLine();
                            if (command.charAt(0) == 'u') {
                                temp.push(ori.pop());
                                System.out.println(ori);
                                System.out.println("Comments created: " + ori.size());
                            } else if (command.charAt(0) == 'r') {
                                ori.push(temp.pop());
                                System.out.println(ori);
                                System.out.println("Comments created: " + ori.size());
                            } else if (command.charAt(0) == 'i')
                                break action;
                            else if (command.charAt(0) == 'x') {
                                if (ori.size() == 0) {
                                    System.out.println();
                                    System.out.println("No comments created");
                                    System.out.println();
                                } else {
                                    for (int i = 0; i < ori.size(); i++) {
                                        try {
                                            JSONParser parser = new JSONParser();
                                            FileReader reader = new FileReader("data.json");
                                            Object obj = parser.parse(reader);
                                            jsonobject0 = (JSONObject) obj;
                                            JSONArray projectarray = (JSONArray) jsonobject0.get("projects");
                                            JSONObject project_obj = (JSONObject) projectarray.get(SelectedProjectId);
                                            JSONArray issuesArray = (JSONArray) project_obj.get("issues");
                                            JSONObject issue = (JSONObject) issuesArray.get(SelectedIssueId);
                                            JSONArray commentArray = (JSONArray) issue.get("comments");
                                            if (commentArray.size() == 0) {
                                                issue.put("status", "In Progress");
                                            }
                                            FileWriter writernewcomment = new FileWriter("data.json");
                                            JSONObject newcomment = new JSONObject();
                                            newcomment.put("comment_id", commentArray.size() + 1);
                                            newcomment.put("text", ori.get(i).text);
                                            JSONArray reactarray = new JSONArray();
                                            JSONObject newreaction1 = new JSONObject();
                                            JSONObject newreaction2 = new JSONObject();
                                            JSONObject newreaction3 = new JSONObject();
                                            JSONObject newreaction4 = new JSONObject();
                                            JSONObject newreaction5 = new JSONObject();
                                            newreaction1.put("reaction", "happy");
                                            newreaction1.put("count", 0);
                                            newreaction2.put("reaction", "thumbsUp");
                                            newreaction2.put("count", 0);
                                            newreaction3.put("reaction", "angry");
                                            newreaction3.put("count", 0);
                                            newreaction4.put("reaction", "sad");
                                            newreaction4.put("count", 0);
                                            newreaction5.put("reaction", "like");
                                            newreaction5.put("count", 0);
                                            reactarray.add(newreaction1);
                                            reactarray.add(newreaction2);
                                            reactarray.add(newreaction3);
                                            reactarray.add(newreaction4);
                                            reactarray.add(newreaction5);
                                            newcomment.put("react", reactarray);
                                            newcomment.put("timestamp", ori.get(i).timestamp);
                                            newcomment.put("user", username);
                                            commentArray.add(newcomment);
                                            issue.put("comments", commentArray);
                                            writernewcomment.write(jsonobject0.toString());
                                            writernewcomment.flush();
                                            writernewcomment.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    UR = false;
                                    break action;
                                }
                                UR = false;
                                UR2 = false;
                            } else {
                                System.out.println("Invalid input");
                                continue;
                            }
                        }
                    }
                } while (UR == true);

                System.out.println("Going back to project dashboard after creating comments...\n");
                Thread.sleep(1500);
                ProjectDashboardJson();
            }

            if (c.charAt(0) == 'l') {
                System.out.println("Logout Successfully!");
                Thread.sleep(500);
                System.exit(0);
            }
            if (c.charAt(0) == 'b') {
                Thread.sleep(1000);
                ProjectDashboardJson();
                return true;
            }
        } else {
            invokeMoreCommand();
            return true;
        }
        return false;
    }

    //new
    public boolean valid(String command) {
        if (command.length() == 1 && (command.charAt(0) == 'r' || command.charAt(0) == 'c' || command.charAt(0) == 'l' || command.charAt(0) == 'b' || command.charAt(0) == 'z' || command.charAt(0) == 'h')) { //||command.charAt(0)=='o'
            return true;
        }
        if (command.equalsIgnoreCase("help")) {
            return true;
        }
        System.err.println("Invalid Command");
        return false;
    }

    //new
    public void invokeAddReaction(JSONArray commentArr) {
        boolean CommentIdValid = false;
        boolean status = false;
        int input = -1;
        String reaction = "";
        while (CommentIdValid == false) {
            Scanner s2 = new Scanner(System.in);
            System.out.println("Enter the comment id to select the comment that you want to react :");
            try {
                input = s2.nextInt();
            } catch (InputMismatchException e) {
                CommentIdValid = false;
            } catch (NumberFormatException e) {
                CommentIdValid = false;
            }
            CommentIdValid = CommentIdExist(commentArr, input);
        }
        SelectedCommentId = input - 1;
        CommentObject = (JSONObject) commentArr.get(input - 1); //getting the comment object
        ReactionArrayInSelectedComment = (JSONArray) CommentObject.get("react"); //getting reaction array
        //System.out.println(CommentObject.toJSONString());
        //System.out.println(ReactionArrayInSelectedComment.toJSONString());
        while (status == false) {
            int cmmnd = -1;
            System.out.println("Reaction List :");
            System.out.println("0 for happy");
            System.out.println("1 for angry");
            System.out.println("2 for thumbsUp");
            System.out.println("3 for sad");
            System.out.println("4 for like");
            Scanner s = new Scanner(System.in);
            System.out.println("Enter your reaction number: ");
            String cmd = s.nextLine();
            try {
                cmmnd = Integer.parseInt(cmd);
            } catch (NumberFormatException e) {
                System.err.println("Invalid Input");
                status = false;
            } catch (InputMismatchException e) {
                System.err.println("Invalid Input");
                status = false;
            }
            reaction = ReactionType(cmmnd);
            if (reaction.isEmpty()) {
                System.out.println();
                System.err.println("Invalid Reaction Type !");
                System.out.println();
                status = false;
            }
            if (!reaction.isEmpty()) {
                status = true;
            }
        }
        if (status == true) {
            //System.out.println(reaction);
            SaveNewReaction(reaction);
        }
    }

    //new
    public void SaveNewReaction(String reaction) {
        boolean found = false;
        long count = -1;
        for (int i = 0; i < ReactionArrayInSelectedComment.size(); i++) {
            JSONObject reactionobject = (JSONObject) ReactionArrayInSelectedComment.get(i);
            String react = (String) reactionobject.get("reaction");
            count = (long) reactionobject.get("count");
           // System.out.println("reaction  " + react + "  count  " + count);
            if (reaction.equals(react)) {
                found = true;
                count = count + 1;
                SelectedReactionType = i;
                //System.out.println("reaction  " + react + "  count  " + count);
                break;
            }

        }
        //System.out.println(SelectedProjectId);
        //System.out.println(SelectedIssueId);
        //System.out.println(SelectedCommentId);
        //System.out.println(SelectedReactionType);
        if (found) {
            try {
                JSONParser parser = new JSONParser();
                FileReader READER = new FileReader("data.json");
                Object obj = parser.parse(READER);
                JSONObject raw = (JSONObject) obj;
                JSONArray projectarr = (JSONArray) raw.get("projects");
                //System.out.println(projectarr);
                JSONObject projectobject = (JSONObject) projectarr.get(SelectedProjectId);
                JSONArray issuesarr = (JSONArray) projectobject.get("issues");
                JSONObject selectedissue = (JSONObject) issuesarr.get(SelectedIssueId);
                JSONArray commentarr = (JSONArray) selectedissue.get("comments");
                JSONObject commentobject = (JSONObject) commentarr.get(SelectedCommentId);
                JSONArray react = (JSONArray) commentobject.get("react");
                JSONObject reactobject = (JSONObject) react.get(SelectedReactionType);
                project_list.get(SelectedProjectId).getIssuesStack().get(SelectedIssueId).CommentStack.get(SelectedCommentId).ReactionList.get(SelectedReactionType).count++;
                reactobject.put("reaction", reaction);
                reactobject.put("count", count);
                FileWriter writer = new FileWriter("data.json");
                writer.write(raw.toString());
                writer.flush();
                writer.close();
                System.out.println("GOING BACK TO PROJECT DASHBOARD AFTER ADDING REACTION...");
                Thread.sleep(1000);
                ProjectDashboardJson();
            } catch (IOException | ParseException | InterruptedException e) {
            }
        }
        if (!found) {
            try {
                FileReader a = new FileReader("data.json");
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(a);
                JSONObject raw = (JSONObject) obj;
                JSONArray projectar = (JSONArray) raw.get("projects");
                JSONObject projectobjec = (JSONObject) projectar.get(SelectedProjectId);
                JSONArray issuesar = (JSONArray) projectobjec.get("issues");
                JSONObject selectedissue = (JSONObject) issuesar.get(SelectedIssueId);
                JSONArray commentar = (JSONArray) selectedissue.get("comments");
                JSONObject commentobjec = (JSONObject) commentar.get(SelectedCommentId);
                Reaction rw = new Reaction(reaction, 1);
                project_list.get(SelectedProjectId).getIssuesStack().get(SelectedIssueId).CommentStack.get(SelectedCommentId).ReactionList.add(rw);
                JSONArray react = (JSONArray) commentobjec.get("react");
                JSONObject reactobject = new JSONObject();
                reactobject.put("reaction", reaction);
                reactobject.put("count", 1);
                react.add(reactobject);
                FileWriter c = new FileWriter("data.json");
                c.write(raw.toString());
                c.flush();
                c.close();
                System.out.println("GOING BACK TO PROJECT DASHBOARD AFTER ADDING REACTION...");
                Thread.sleep(1000);
                ProjectDashboardJson();
            } catch (IOException | ParseException | InterruptedException e) {
                System.err.println("ERROR");
            }
        }
    }

    //new
    public boolean CommentIdExist(JSONArray commentArr, int index) {
        boolean status = false;
        try {
            JSONObject comment = (JSONObject) commentArr.get(index - 1);
            status = true;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid Input");
            return false;
        }
        return status;
    }

    //new
    public String ReactionType(int x) {
        String reaction = "";
        if (x == 0) {
            reaction = "happy";
        }
        if (x == 1) {
            reaction = "angry";
        }
        if (x == 2) {
            reaction = "thumbsUp";
        }
        if (x == 3) {
            reaction = "sad";
        }
        if (x == 4) {
            reaction = "like";
        }
        return reaction;
    }

    //new
    public void invokeMoreCommand() throws FileNotFoundException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("List of extra commands :");
        System.out.println("'s' to change status");
        System.out.println("'l' to logout");
        System.out.println("'b' to project dashboard");
        System.out.println("'z' to generate a report ");
        System.out.println("'h' to invoke chat feature ");
        while (true) {
            String c = sc.nextLine();
            if (c.charAt(0) == 'l') {
                System.out.println("Logout Successfully! ");
                System.exit(0);
            } else if (c.charAt(0) == 'b') {
                ProjectDashboardJson();
            } else if (c.charAt(0) == 'h') {
                MainScreen m1 = new MainScreen();
                m1.show();
                MainScreen m2 = new MainScreen();
                m2.show();
                ProjectDashboardJson();
            } else if (c.charAt(0) == 'z') {
                GenerateReport();
                System.out.println("GOING BACK TO PROJECT DASHBOARD AFTER GENERATING REPORT....");
                Thread.sleep(1000);
                System.out.println();
                System.out.println();
                ProjectDashboardJson();
            } else if (c.charAt(0) == 's') {
                try {
                    JSONParser parser = new JSONParser();
                    FileReader reader = new FileReader("data.json");
                    Object obj = parser.parse(reader);
                    jsonobject0 = (JSONObject) obj;
                    JSONArray projectarray = (JSONArray) jsonobject0.get("projects");
                    JSONObject project_obj = (JSONObject) projectarray.get(SelectedProjectId);
                    JSONArray issuesArray = (JSONArray) project_obj.get("issues");
                    JSONObject issue = (JSONObject) issuesArray.get(SelectedIssueId);
                    System.out.println("\nChanging Status....");
                    Thread.sleep(700);
                    System.out.println("Choose the number that corresponds to the status: ");
                    System.out.println("Open - 1");
                    System.out.println("In Progress - 2");
                    System.out.println("Reopened - 3");
                    System.out.println("Resolved - 4");
                    System.out.println("Closed - 5");
                    FileWriter statusChanger = new FileWriter("data.json");
                    c = sc.nextLine();
                    switch (c) {
                        case "1":
                            issue.put("status", "Open");
                            break;
                        case "2":
                            issue.put("status", "In Progress");
                            break;
                        case "3":
                            issue.put("status", "Reopened");
                            break;
                        case "4":
                            issue.put("status", "Resolved");
                            break;
                        case "5":
                            issue.put("status", "Closed");
                            break;
                        default:
                            System.err.println("Invalid input, going back to project dashboard...");
                            ProjectDashboardJson();
                            break;
                    }
                    statusChanger.write(jsonobject0.toString());
                    statusChanger.flush();
                    statusChanger.close();
                    System.out.println("Going back to project dashboard after changing status\n");
                    ProjectDashboardJson();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                System.err.println("Invalid Command, enter again.");
        }
    }

    public int search(JSONArray issuesarr, String input) {
        for (int i = 0; i < issuesarr.size(); i++) {
            JSONObject issue = (JSONObject) issuesarr.get(i);
            String title = (String) issue.get("title");
            if (title.contains(input)) {
                return i;
            }
        }
        return -1;
    }

    public boolean DisplayIssuesListJson(String id) {
        try {
            JSONParser parser = new JSONParser();
            FileReader file = new FileReader("data.json");
            Object obj = parser.parse(file);
            jsonobject0 = (JSONObject) obj;
            projectsarray = (JSONArray) jsonobject0.get("projects");
            int index = Integer.parseInt(id);
            index -= 1;
            JSONObject project = (JSONObject) projectsarray.get(index);
            JSONArray issuesarray = (JSONArray) project.get("issues");
            issuesarray.forEach(a -> ReadIssuesListsJson((JSONObject) a));
        } catch (IndexOutOfBoundsException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        } catch (InputMismatchException e) {
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void ReadIssuesListsJson(JSONObject a) {
        JSONObject raw = a;
        long id = (long) raw.get("id");
        String title = (String) raw.get("title");
        String status = (String) raw.get("status");
        JSONArray tag = (JSONArray) raw.get("tag");
        String tags = "";
        for (int i = 0; i < tag.size(); i++) {
            tags += (String) tag.get(i);
        }
        long priority = (long) raw.get("priority");
        String timestamp = (String) raw.get("timestamp");
        String assignee = (String) raw.get("assignee");
        String creator = (String) raw.get("createdBy");
        System.out.printf(RED+"| %-3s%-3s%-33s%-4s%-12s%-6s%-17s%-3s%-8s%-4s%-18s%-5s%-9s%-5s%-8s%-2s\n", id,"|",title,"|", status,"|  " ,tags,"|       " ,priority,"| " ,timestamp,"|",assignee,"|",creator,"|");
        System.out.println("+----+-----------------------------------+---------------+----------------------+---------------+---------------------+-------------+------------+");
        System.out.print(RESET);
    }

    public void ReadUserData() throws FileNotFoundException {
        JSONParser parser = new JSONParser();
        try {
            FileReader file = new FileReader("data.json");
            Object obj = parser.parse(file);
            jsonobject0 = (JSONObject) obj;
            userarray = (JSONArray) jsonobject0.get("users");
            userarray.forEach(a -> GetUserData((JSONObject) a));
            //System.out.println(userarray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void GetUserData(JSONObject a) {
        JSONObject empobj = a;
        Long userid = (Long) empobj.get("userid");
        String username = (String) empobj.get("username");
        String password = (String) empobj.get("password");
        //System.out.println("userid : "+ userid);
        //System.out.println("username : "+username);
        //System.out.println("password : "+password);
        user NewUser = new user(username, password); //create new user based on info accessed using key
        user_list.add(NewUser); //arraylist.add user
    }

    public void register() throws FileNotFoundException, InterruptedException {
        boolean status = true;
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("""
                Hello, New User !
                Welcome to our Bug Tracking System !
                Kindly register to use our System !        
                     """);
        while (status == true) {
            System.out.println("Enter Your Username : ");
            String username = sc.nextLine();
            String password = "";
            String cpassword = "";
            do {
                System.out.println("Enter Your Password : ");
                password = sc.nextLine();
                System.out.println("Please confirm your password");
                cpassword = sc.nextLine();
            } while (!password.equalsIgnoreCase(cpassword));
            //System.out.println(RepeatedUsername(username));
            boolean repeated = RepeatedUsername(username);
            if (repeated == true) {
                System.err.println("Username has been used ! Pls try again !");
                System.out.println();
                Thread.sleep(1000);
                status = repeated;
            }
            if (repeated == false) {
                user newuser = new user(username, password);
                user_list.add(newuser);
                System.out.println();
                System.out.println("Successfully registered !!!");
                SaveNewUser(newuser);
                status = false;
            }
        }
    }

    public void SaveNewUser(user n) {
        JSONParser parser = new JSONParser();
        try {
            FileReader file = new FileReader("data.json");
            Object obj = parser.parse(file);
            jsonobject0 = (JSONObject) obj;
            userarray = (JSONArray) jsonobject0.get("users");
            JSONObject finall = jsonobject0;
            FileWriter wr = new FileWriter("data.json");
            JSONObject raw = new JSONObject();
            raw.put("userid", n.userid);
            raw.put("username", n.username);
            raw.put("password", n.password);
            userarray.add(raw);
            finall.put("users", userarray);
            wr.write(finall.toJSONString());
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean RepeatedUsername(String username) {
        boolean status = false;
        for (int i = 0; i < user_list.size(); i++) {
            if (user_list.get(i).username.equals(username)) {
                status = true;
                break;
            }
        }
        return status;
    }

    public boolean UserExist(String username, String password) {
        boolean status = false;
        for (int i = 0; i < user_list.size(); i++) {
            if (user_list.get(i).getUsername().equals(username) && user_list.get(i).getPassword().equals(password)) {
                status = true;
                break;
            }
        }
        return status;
    }

    public void checkPerformance() throws FileNotFoundException {
        ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
        String username = "";
        String topperformer = "";
        int max = -1;
        for (int i = 0; i < raw.size(); i++) {
            Projects p = raw.get(i);
            int score;
            Stack<Issues> s = p.getIssuesStack();
            for (int j = 0; j < s.size(); j++) {
                Issues is = s.get(j);
                if (is.status.equalsIgnoreCase("Resolved")) {
                    username = is.assignee;
                    // System.out.println(username);
                    for (int k = 0; k < user_list.size(); k++) {
                        if (user_list.get(k).username.equalsIgnoreCase(username)) {
                            user u = user_list.get(k);
                            score = u.score;
                            //System.out.println(score);
                            score += 1;
                            //System.out.println(score);
                            u.setScore(score);
                        }
                    }

                }
            }
        }
        for (int m = 0; m < user_list.size(); m++) {
            user usr = user_list.get(m);
            int scr = usr.getScore();
            if (scr > max) {
                max = scr;
                topperformer = usr.username;
            }
        }
        System.out.println();
        System.out.println("Top Performer is : " + topperformer);
        System.out.println();
        System.out.println("Issues Solved : " + max);
        System.out.println();
    }

    public void GenerateReport() throws FileNotFoundException {
        System.out.println(PURPLE);
        System.out.println(" GENERAL REPORT ON THE PROJECTS AND ISSUES ");
        System.out.println(" ----------------------------------------- ");
        System.out.print(RESET);
        System.out.print(BLUE);
        System.out.println(" PROJECTS ");
        System.out.println(" -------- ");
        checkProjectStatus();
        System.out.print(RESET);
        System.out.println(RED);
        System.out.println(" ISSUES ");
        System.out.println(" ------ ");
        checkTagFrequency();
        System.out.print(RESET);
        System.out.println(GREEN);
        System.out.println(" TOP PERFORMER ");
        System.out.println(" ------------- ");
        checkPerformance();
        System.out.print(RESET);
        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER ANY KEY TO CONTINUE: ");
        String c = sc.nextLine();
        if (c != null) {
            System.out.println();
        }
        PieChartPlot b = new PieChartPlot();
        String[] b2 = main.gg;
        b.runn(b2);

    }


    public void checkTagFrequency() throws FileNotFoundException {
        ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
        int countFrontEnd = 0;
        int countBackEnd = 0;
        int countCMTY = 0;
        String mostFrequentTag = "";
        for (int i = 0; i < raw.size(); i++) {
            Projects p = raw.get(i);
            Stack<Issues> issuesStack = p.issuesStack;
            for (int j = 0; j < issuesStack.size(); j++) {
                Issues is = issuesStack.get(j);
                String tag = is.tag;
                if (tag.equalsIgnoreCase("Frontend")) {
                    countFrontEnd++;
                }
                if (tag.equalsIgnoreCase("Backend")) {
                    countBackEnd++;
                }
                if (tag.equalsIgnoreCase("cmty:bug-report")) {
                    countCMTY++;
                }
            }
        }
        System.out.println("Number of 'Frontend' Tag: " + countFrontEnd);
        System.out.println();
        System.out.println("Number of 'Backend' Tag: " + countBackEnd);
        System.out.println();
        System.out.println("Number of 'cmty:bug-report' Tag: " + countCMTY);
        System.out.println();
        if (countFrontEnd == countBackEnd && countFrontEnd == countCMTY) {
            mostFrequentTag = "Frontend, Backend, and cmty:bug-report";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countFrontEnd);
        } else if (countFrontEnd > countBackEnd && countFrontEnd > countCMTY) {
            mostFrequentTag = "Frontend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countFrontEnd);
        } else if (countFrontEnd > countBackEnd && countFrontEnd == countCMTY) {
            mostFrequentTag = "Frontend and cmty:bug-report";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countFrontEnd);
        } else if (countFrontEnd == countBackEnd && countFrontEnd > countCMTY) {
            mostFrequentTag = "Frontend and Backend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countFrontEnd);
        } else if (countBackEnd > countFrontEnd && countBackEnd > countCMTY) {
            mostFrequentTag = "Backend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countBackEnd);
        } else if (countBackEnd > countFrontEnd && countBackEnd == countCMTY) {
            mostFrequentTag = "Backend and cmty:bug-report";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countBackEnd);
        } else if (countBackEnd == countFrontEnd && countBackEnd > countCMTY) {
            mostFrequentTag = "Backend and Frontend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countBackEnd);
        } else if (countCMTY > countFrontEnd && countCMTY > countBackEnd) {
            mostFrequentTag = "cmty:bug-report";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countCMTY);
        } else if (countCMTY == countFrontEnd && countCMTY > countBackEnd) {
            mostFrequentTag = "cmty:bug-report and Frontend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countCMTY);
        } else if (countCMTY > countFrontEnd && countCMTY == countBackEnd) {
            mostFrequentTag = "cmty:bug-report and Backend";
            System.out.println("Most Frequent Tag is : " + mostFrequentTag + " with a count of " + countCMTY);
        }

    }

    public void checkProjectStatus() throws FileNotFoundException {
        ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
        int resolved = 0;
        int in_progress = 0;
        int closed = 0;
        int open = 0;
        int reopened = 0;
        for (int i = 0; i < raw.size(); i++) {
            Projects pj = raw.get(i);
            Stack<Issues> issuesStack = pj.issuesStack;
            for (int j = 0; j < issuesStack.size(); j++) {
                Issues is = issuesStack.get(j);
                String status = is.status;
                if (status.equalsIgnoreCase("Resolved")) {
                    resolved++;
                }
                if (status.equalsIgnoreCase("In Progress")) {
                    in_progress++;
                }
                if (status.equalsIgnoreCase("closed")) {
                    closed++;
                }
                if (status.equalsIgnoreCase("Open")) {
                    open++;
                }
                if (status.equalsIgnoreCase("Reopened")) {
                    reopened++;
                }

            }
        }
        System.out.println("Number of projects that are resolved: " + resolved);
        System.out.println();
        System.out.println("Number of projects that are in progress: " + in_progress);
        System.out.println();
        System.out.println("Number of projects that are closed: " + closed);
        System.out.println();
        System.out.println("Number of projects that are open: " + open);
        System.out.println();
        System.out.println("Number of projects that are reopened: " + reopened);
        System.out.println();
    }


}