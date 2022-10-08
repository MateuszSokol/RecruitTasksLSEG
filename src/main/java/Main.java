import excel.XlsReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        final String LINK_FOR_FIRST_EXERCISE = "https://www.ote-cr.cz/en/statistics/electricity-imbalances?version=0&date=2022-09-26";
        FileWriter htmlOut = new FileWriter("src\\main\\java\\output.html");

        final String LINK_FOR_SECOND_EXERCISE = "https://www.ote-cr.cz/pubweb/attachments/05_09_12/2022/month09/day28/Imbalances_28_09_2022_V0_EN.xls";

        final String LINK_FOR_THIRD_EXERCISE ="https://www.okg.se/en";

        final String LINK_FOR_FOURTH_EXERCISE ="https://www.nhc.noaa.gov/data/hurdat/hurdat2-nepac-1949-2016-041317.txt";

        File out = new File("src\\main\\java\\Excel file.xls");

        //task1
       // task1(LINK_FOR_FIRST_EXERCISE,htmlOut);

        //task2
       // new Thread(new Download(LINK_FOR_SECOND_EXERCISE,out)).start();
        //task2
     //   XlsReader xlsReader = new XlsReader();
      //  xlsReader.Reader();
        //task3
      //  task3(LINK_FOR_THIRD_EXERCISE);
        //task4
        task4(LINK_FOR_FOURTH_EXERCISE);
    }
    private static void task1(String url,FileWriter out) throws IOException {

        StringBuilder str = new StringBuilder();

        try{
            final Document document = Jsoup.connect(url).get();
            for(Element element: document.select("div.bigtable.left-sticky")){
                str.append(element.html());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedWriter bufferedWriter;


        bufferedWriter = new BufferedWriter(out);
        bufferedWriter.write(str.toString());
        bufferedWriter.close();

    }

    private static void task3(String url){

        WebDriver driver;
        System.setProperty("webdriver.chrome.driver","src\\main\\java\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(url);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();

        try{

            WebElement element = driver.findElement(By.className("the-gauge"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(10000);
            System.out.println("value:"+element.getText()+", "+"time:"+dtf.format(now));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void task4(String link){

     //   String [] stormNames = new String[4];
        int [] maxWindValueArr = new int[4];
        int hurricaneNamesIterator =0;
        int maxWindSpeedIterator= 0;
        String substringPiece = null;
        List<Integer> maxWindValueList = new LinkedList<>();
        List<String> hurricaneNamesList = new ArrayList<>();
        List<Integer> maxHurricaneSpeedKnotValueList = new LinkedList<>();
        List<Integer> hurricaneSpdResultAList = new LinkedList<>();

        try {

            URL url = new URL(link);
            Pattern pattern =Pattern.compile("2016");

            Scanner s = new Scanner(url.openStream());
            // read from your scanner
            s.useDelimiter(",");

            int cnt = 0;
            while(s.hasNextLine()) {
                String scannerOut = s.nextLine();
                String sc = scannerOut.replaceAll("\\s", "");


                Matcher matcher = pattern.matcher(scannerOut);
                boolean matchFound = matcher.find();
                if (matchFound) {
                    if (sc.charAt(0) == 'E') {
                        int firstLetterStormNameIndex = sc.indexOf(',') + 1;
                        substringPiece = sc.substring(firstLetterStormNameIndex);
                        int lastLetterStormNameIndex = substringPiece.indexOf(',');
                        substringPiece = substringPiece.substring(0, lastLetterStormNameIndex);
                        if (substringPiece.charAt(substringPiece.length() - 1) == 'A') {
                         //   stormNames[hurricaneNamesIterator] = substringPiece;
                            hurricaneNamesList.add(substringPiece);

                        }
                    }
                }

                if(cnt<hurricaneNamesList.size() && hurricaneNamesList.get(cnt) !=null ){
                    int index = hurricaneNamesList.size()-cnt;
                    Pattern pattern1 = Pattern.compile(hurricaneNamesList.get(cnt));

                    Matcher matcher1 = pattern1.matcher(sc);
                    boolean match = matcher1.find();

                    if (match) {

                        while (s.hasNextLine()) {


                            String keepValueFromTextNextLine = s.nextLine();

                            if (keepValueFromTextNextLine.contains("E")) {
                                break;
                            } else {
                                keepValueFromTextNextLine = keepValueFromTextNextLine.replaceAll("\\s", "");
                                int firstIndexOfW = keepValueFromTextNextLine.indexOf("W") + 2;
                                String sPiece = keepValueFromTextNextLine.substring(firstIndexOfW);
                                int lastIndexComma = sPiece.indexOf(",");
                                String resultStr = sPiece.substring(0, lastIndexComma);
                                if (!resultStr.equalsIgnoreCase("")) {
                                    int resultInt = Integer.parseInt(resultStr);
                                    maxHurricaneSpeedKnotValueList.add(resultInt);

                                }
                            }

                        }
                        Collections.sort(maxHurricaneSpeedKnotValueList);
                        for (int i = maxHurricaneSpeedKnotValueList.size()-1;  ; i--) {
                            if(maxHurricaneSpeedKnotValueList.get(i) !=null){
                                hurricaneSpdResultAList.add(maxWindSpeedIterator,maxHurricaneSpeedKnotValueList.get(i));
                                maxWindSpeedIterator++;
                                maxHurricaneSpeedKnotValueList.clear();
                                break;
                            }
                        }


                    }

                    cnt++;

                }

            }
            for (int i = 0; i <hurricaneNamesList.size(); i++) {
                System.out.println("Hurricane name: "+ hurricaneNamesList.get(i) + " Max wind speed(in knots): "+ hurricaneSpdResultAList.get(i));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


