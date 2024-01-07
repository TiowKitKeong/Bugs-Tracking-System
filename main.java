import java.io.FileNotFoundException;

public class main {
    static String[] gg;
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        gg=args;
        UserInterface a = new UserInterface();
        String file = "bensound-love.wav";
        a.playMusic(file);
        a.runProgram();

    }
}
