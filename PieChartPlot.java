
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Stack;

public class PieChartPlot extends Application {
    int resolved = 0;
    int in_progress = 0;
    int closed = 0;
    int open = 0;
    int reopened = 0;
    int countFrontEnd = 0;
    int countBackEnd = 0;
    int countCMTY = 0;
    int is2017=0;
    int is2018=0;
    int is2019=0;
    int is2020=0;
    int is2021=0;
    int p1=0;
    int p2=0;
    int p3=0;
    int p4=0;
    int p5=0;
    int p6=0;
    int p7=0;
    int p8=0;
    int p9=0;

    public void checkPriority() throws FileNotFoundException {
        DataStorage a = new DataStorage();
        a.ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
        for (int i = 0; i < raw.size(); i++) {
            Projects p = raw.get(i);
            Stack<Issues> issuesStack = p.issuesStack;
            for (int j = 0; j < issuesStack.size(); j++) {
                Issues is = issuesStack.get(j);
                String status = is.status;
                 long priority = is.priority;
                 if(priority==1&&(status.equalsIgnoreCase("Closed")==false)){p1++;}
                if(priority==2&&(status.equalsIgnoreCase("Closed")==false)){p2++;}
                if(priority==3&&(status.equalsIgnoreCase("Closed")==false)){p3++;}
                if(priority==4&&(status.equalsIgnoreCase("Closed")==false)){p4++;}
                if(priority==5&&(status.equalsIgnoreCase("Closed")==false)){p5++;}
                if(priority==6&&(status.equalsIgnoreCase("Closed")==false)){p6++;}
                if(priority==7&&(status.equalsIgnoreCase("Closed")==false)){p7++;}
                if(priority==8&&(status.equalsIgnoreCase("Closed")==false)){p8++;}
                if(priority==9&&(status.equalsIgnoreCase("Closed")==false)){p9++;}
            }
        }
    }

    public void checkTime() throws FileNotFoundException {
        DataStorage a = new DataStorage();
        a.ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
        for (int i = 0; i < raw.size(); i++) {
            Projects p = raw.get(i);
            Stack<Issues> issuesStack = p.issuesStack;
            for (int j = 0; j < issuesStack.size(); j++) {
                Issues is = issuesStack.get(j);
                String timestamp=is.timestamp;
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime date = LocalDateTime.parse(timestamp,myFormatObj);
                int x = date.getYear();
                if(x==2017){is2017++;}
                if(x==2018){is2018++;}
                if(x==2019){is2019++;}
                if(x==2020){is2020++;}
                if(x==2021){is2021++;}

            }
        }
    }
    public void checkTagFrequency() throws FileNotFoundException {
        DataStorage a = new DataStorage();
        a.ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
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
    }



    public void checkProjectStatus() throws FileNotFoundException {
        DataStorage a = new DataStorage();
        a.ReadUserData();
        CreateObjectFromJson b = new CreateObjectFromJson();
        b.json();
        ArrayList<Projects> raw = b.project_list; //obtain project list from json file
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
    }
        @Override
        public void start(Stage stage) throws FileNotFoundException {
            checkProjectStatus();
            checkTagFrequency();
            checkTime();
            checkPriority();

            //Preparing ObservbleList object
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("RESOLVED", resolved),
                    new PieChart.Data("IN PROGRESS", in_progress),
                    new PieChart.Data("CLOSED", closed),
                    new PieChart.Data("REOPENED", reopened),
                    new PieChart.Data("OPEN", open));
            ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList(
                    new PieChart.Data("Frontend", countFrontEnd),
                    new PieChart.Data("Backend", countBackEnd),
                    new PieChart.Data("cmty:bug-report", countCMTY));
            ObservableList<PieChart.Data> pieChartData3 = FXCollections.observableArrayList(
                    new PieChart.Data("2017", is2017),
                    new PieChart.Data("2018", is2018),
                    new PieChart.Data("2019", is2019),
                    new PieChart.Data("2020", is2020),
                    new PieChart.Data("2021", is2021));
            ObservableList<PieChart.Data> pieChartData4 = FXCollections.observableArrayList(
                    new PieChart.Data("1", p1),
                    new PieChart.Data("2", p2),
                    new PieChart.Data("3", p3),
                    new PieChart.Data("4", p4),
                    new PieChart.Data("5", p5),
                    new PieChart.Data("6", p6),
                    new PieChart.Data("7", p7),
                    new PieChart.Data("8", p8),
                    new PieChart.Data("9", p9));


            //Creating a Pie chart
            PieChart pieChart = new PieChart(pieChartData);
            PieChart pieChart2 = new PieChart(pieChartData2);
            PieChart pieChart3 = new PieChart(pieChartData3);
            PieChart pieChart4 = new PieChart(pieChartData4);

            //Setting the title of the Pie chart
            pieChart.setTitle(" Number of Issues According To Status ");
            pieChart2.setTitle(" Frequency of Tags ");
            pieChart3.setTitle(" Number of Issues Created According To Years ");
            pieChart4.setTitle(" Number of Remaining Issues According to Priority");

            //setting the direction to arrange the data
            pieChart.setClockwise(true);
            pieChart2.setClockwise(true);
            pieChart3.setClockwise(true);
            pieChart4.setClockwise(true);

            //Setting the length of the label line
            pieChart.setLabelLineLength(52);
            pieChart2.setLabelLineLength(52);
            pieChart3.setLabelLineLength(52);
            pieChart4.setLabelLineLength(52);

            //Setting the labels of the pie chart visible
            pieChart.setLabelsVisible(true);
            pieChart2.setLabelsVisible(true);
            pieChart3.setLabelsVisible(true);
            pieChart4.setLabelsVisible(true);

            //Setting the start angle of the pie chart
            pieChart.setStartAngle(180);
            pieChart2.setStartAngle(180);
            pieChart3.setStartAngle(180);
            pieChart4.setStartAngle(180);

            FlowPane root = new FlowPane();
            root.getChildren().addAll(pieChart,pieChart2,pieChart3,pieChart4);
                //Creating a Group object
               // Group root = new Group(pieChart);

                //Creating a scene object
                Scene scene = new Scene(root, 1000, 800);

                //Setting title to the Stage
                stage.setTitle("REPORT STATISTIC");

                //Adding scene to the stage
                stage.setScene(scene);

                //Displaying the contents of the stage
                stage.show();

        }
        public void runn(String[] args){
        launch(args);
        }

    }

