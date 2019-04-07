import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Text {
    public static void main(String[] args) throws IOException {
        int num = 4; /*Integer.parseInt(args[0]);*/
        System.out.println("debug " + num);

        String fileName1 = "text1.txt";
        String fileName2 = "text2.txt";

        // читаем файл в строку с помощью класса Files, удаляем спец символы
        String text1 = removeSpecialSymbols(getStringFromFile(fileName1));
        String text2 = " " + removeSpecialSymbols(getStringFromFile(fileName2)) + " .";

        //кол-во подзадач
        int taskCount=(int)Math.ceil(text1.length()/300f);

        //вычисляем индексы начала и конца подстроки
        int ind1 = text1.length() / taskCount * num;
        int ind2;
        if (num == taskCount-1) {
            ind2 = text1.length() - 1;
        } else ind2 = text1.length() / taskCount * (num + 2);

        String subText = " " + text1.substring(ind1, ind2) + " .";

        List<String> common = getCommonSubstrings(subText, text2);

        File f = new File("out" + num + ".txt");
        f.createNewFile();
        FileWriter fw = new FileWriter(f);
        for (String str : common) {
            fw.append(str + "\n");
        }
        fw.close();
    }

    private static String getStringFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)), "UTF-8");
    }

    private static String removeSpecialSymbols(String text) {
        String textOut = text.replaceAll("\\p{Cntrl}", " ");
        String textOut2 = textOut.replaceAll("  ", " ");
        String textOut3 = textOut2.replaceAll("\uFEFF", "");
        return textOut3;
    }

    private static List<Integer> getListOfIndexes(String text) {
        List<Integer> wordsStartStr = new ArrayList<>();
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == ' ' & text.substring(i, i + 1).matches(".") & text.charAt(i + 1) != ' ') {
                wordsStartStr.add(i + 1);
            }
        }
        return wordsStartStr;
    }

    //метод, вычисляющий общие строки
    public static List<String> getCommonSubstrings(String str1, String str2) {
        String s1 = "";
        String s2 = "";

        List<String> excerpts = new ArrayList<>();

        //лист индексов первых букв для str1
        List<Integer> wordsStartStr1 = getListOfIndexes(str1);

        //лист индексов первых букв для str2
        List<Integer> wordsStartStr2 = getListOfIndexes(str2);

        for (int i = 0; i < wordsStartStr1.size() - 1; i++) {
            for (int j = 0; j < wordsStartStr2.size() - 1; j++) {
                s1 = str1.substring(wordsStartStr1.get(i), wordsStartStr1.get(i + 1));
                s2 = str2.substring(wordsStartStr2.get(j), wordsStartStr2.get(j + 1));
                if (s1.equals(s2)) {
                    String commonLine = str1.substring(wordsStartStr1.get(i), wordsStartStr1.get(i + 1));
                    i++;
                    j++;

                    int count = 0;

                    while (i < wordsStartStr1.size()-1 && j < wordsStartStr2.size()-1
                            && str1.substring(wordsStartStr1.get(i), wordsStartStr1.get(i + 1)).equals(str2.substring(wordsStartStr2.get(j), wordsStartStr2.get(j + 1)))) {

                        commonLine += str1.substring(wordsStartStr1.get(i), wordsStartStr1.get(i + 1));
                        count++;
                        i++;
                        j++;
                    }
                    excerpts.add(commonLine.trim());
                    if (count > 0) {
                        i -= count;
                        j -= count;
                    } else {
                        i--;
                        j--;
                    }
                }
            }

        }

        //удаляем совпадения, которые включены в другие, более крупные
        for (int i = 0; i < excerpts.size()-1; i++) {
            for (int j = 0; i < excerpts.size()-1; j++) {
                if (j == excerpts.size()) {
                    break;
                }
                if (i != j && excerpts.get(i).contains(excerpts.get(j))) {
                    excerpts.remove(j);
                    j--;
                }
            }
        }

        return excerpts;
    }

}



