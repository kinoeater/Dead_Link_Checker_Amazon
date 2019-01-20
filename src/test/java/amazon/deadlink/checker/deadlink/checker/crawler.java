package amazon.deadlink.checker.deadlink.checker;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class crawler {


    static WebDriver driver = new ChromeDriver();

    public static void main(String[] args) throws InterruptedException, AWTException {

       
        driver.manage().window().maximize();
        driver.get("https://www.amazon.com/");
      
        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='nav-link-shopall']/span[2]")).click();
        Thread.sleep(3000);

        List < WebElement > links = driver.findElements(By.tagName("a")); // List of the Web Elements that has the "a" tag,
        //which is in this case helps up to find the links


        String idForTxtFile = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss").format(new Date()); //create a stamp for the time being
        //combine the stamp and file name-path

        String path_name_file = System.getProperty("user.dir") + "\\target\\Results_" + idForTxtFile;
        System.out.println(path_name_file+" is created!");


        for (int i = 0; i < links.size(); i++) { //for loop to examine each link, each instance
            WebElement ele = links.get(i); // will use verifyLinkActive method separately
            String url = ele.getAttribute("href");
            verifyLinkActive(url, path_name_file);
        }


        uploadDropbox(path_name_file);

    }


    //*****method to verify the active link **************/

    public static void verifyLinkActive(String linkUrl, String file) {


        try {
            URL url = new URL(linkUrl);

            HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();

            httpURLConnect.setConnectTimeout(3000);

            httpURLConnect.connect();

            if (httpURLConnect.getResponseCode() == 200) {

                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage());
                String output = linkUrl + " - " + httpURLConnect.getResponseMessage();
                FileWriter writer = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(writer);
                br.write("\r\n"); // write new line
                br.write(output);
                br.close();


            }
            if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + " - " + HttpURLConnection.HTTP_NOT_FOUND);
                String output = linkUrl + " - " + httpURLConnect.getResponseMessage() + " - " + HttpURLConnection.HTTP_NOT_FOUND;
                FileWriter writer = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(writer);
                br.write("\r\n"); // write new line
                br.write(output);
                br.close();
            }
        } catch (Exception e) {

        }


    }

    //*****method to upload the results file **************/

    public static void uploadDropbox(String path_filename) throws InterruptedException, AWTException {

        driver.get("https://www.dropbox.com/en_GB/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);

        By UploadFile = By.xpath("//div[@class='ue-effect-container uee-AppActionsView-SecondaryActionMenu-text-upload-file']");
        By username = By.name("login_email");
        By password = By.name("login_password");
        By loginbutton = By.xpath("//div[@class='signin-text']");
        driver.findElement(username).sendKeys("mark@eftsoftware.com");
        Thread.sleep(3000);
        driver.findElement(password).sendKeys("Password1!");
        Thread.sleep(3000);
        driver.findElement(loginbutton).click();
        Thread.sleep(3000);
        driver.get("https://www.dropbox.com/home/results");
        Thread.sleep(5000);
        driver.findElement(UploadFile).click();
        Thread.sleep(3000);

        
        StringSelection ss = new StringSelection(path_filename);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(3000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        Thread.sleep(5000);



    }



}