import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Grid {
    public static void main(String[] args) throws Exception {
        File fileJDF = new File("myJob.jdf");
        fileJDF.createNewFile();
        FileWriter JdfWriter = new FileWriter(fileJDF);
        JdfWriter.append("job:\n");
        JdfWriter.append("\tname: sum\n");
        JdfWriter.append("\tinit:\n");
        JdfWriter.append("\t\tput text1.txt\n");
        JdfWriter.append("\t\tput text2.txt\n");
        JdfWriter.append("\t\tput Text.class Text.class\n");

        String text1 = new String(Files.readAllBytes(Paths.get("text1.txt")), "UTF-8");
        String text2 = new String(Files.readAllBytes(Paths.get("text2.txt")), "UTF-8");

        int taskCount=(int)Math.ceil(text1.length()/300f);

        System.out.println("Subtask count is " + taskCount);

        for (int i = 0; i < taskCount; i++) {
            JdfWriter.append("task: remote: java grid.Sum " + i + "\n");
            JdfWriter.append("\t  final: get out" + i + ".txt out" + i + ".txt\n");
        }

        JdfWriter.close();
        //произошла и jdf запихнулся в грид, началось выполнение
        //ждем когда он нам вернет файлы результатов
        List<String> commonStrings = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            File out = new File("out" + i + ".txt");
            while (!out.exists()) Thread.sleep(100);
            Scanner scOut = new Scanner(out);
            while (scOut.hasNextLine()) {
                String s = scOut.nextLine();
                commonStrings.add(s);
            }
        }

        //удаляем совпадения, которые включены в другие, более крупные
        for (int i = 0; i < commonStrings.size()-1; i++) {
            for (int j = 0; i < commonStrings.size()-1; j++) {
                if (j == commonStrings.size()) {
                    break;
                }
                if (i != j && commonStrings.get(i).contains(commonStrings.get(j))) {
                    commonStrings.remove(j);
                    j--;
                }
            }
        }

        for (String str : commonStrings) {
            System.out.println(str);
        }
    }
}
