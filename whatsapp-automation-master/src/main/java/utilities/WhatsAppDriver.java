package utilities;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The {@link WhatsAppDriver} class is and API for taking actions in the
 * WhatsApp Web client.
 * 
 * @author Asaf Karavani
 *
 */
public class WhatsAppDriver {

	WebDriver driver;
	Wait<WebDriver> longWait;
	Wait<WebDriver> shortWait;
	boolean connected;

	/**
	 * 
	 * @param browser
	 *            takes a {@link Browser} Enumeration to determine which browser
	 *            to use (e.g. {@link Browser.CHROME}, {@link Browser.FIREFOX},
	 *            {@link Browser.PHANTOMJS}, etc.)
	 */
	public WhatsAppDriver(Browser browser) {
		if (browser == Browser.CHROME) {
			driver = new ChromeDriver();
		} else if (browser == Browser.FIREFOX) {
			driver = new FirefoxDriver();
		} else if (browser == Browser.PHANTOMJS) {
			driver = new PhantomJSDriver();
		} else {
			// Default Browser
			driver = new ChromeDriver();
		}

		longWait = new WebDriverWait(driver, 30);
		shortWait = new WebDriverWait(driver, 3);

		connected = false;
	}

	/**
	 * Will open the WhatApp Web client.
	 */
	public void open() {
		driver.get("https://web.whatsapp.com/");

	}

	public void close() {
		driver.close();

	}

	public void quit() {
		driver.quit();

	}

	public void openConvWith(String contactName) {
		WebElement searchBar = driver.findElement(By.cssSelector(".input.input-search"));
		searchBar.clear();
		searchBar.sendKeys(contactName);

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".icon.icon-x-alt")));
		WebElement conv = driver.findElement(By.xpath("//*[@id=\"pane-side\"]/div[1]/div/div/div[1]"));
		conv.click();

		try {
			shortWait.until(ExpectedConditions.presenceOfElementLocated(By.className("incoming-msgs")));
			driver.findElement(By.className("incoming-msgs")).click();
		} catch (Exception e) {
			System.out.println("Can't find Scrolldown button.");
		}

	}

	public void waitForConnection() throws TimeoutException {
		try {
			longWait.until(ExpectedConditions.presenceOfElementLocated(By.className("intro-title")));
			// TimeUnit.SECONDS.sleep(3);
			connected = true;
			System.out.println("Connected.");

		} catch (TimeoutException t) {
			connected = false;
			throw t;
		}

	}

	public String getCurrentConvImg() {
		By byConvImg = By.xpath("//*[@id=\"main\"]/header/div[1]/div/div/img");
		WebElement img = driver.findElement(byConvImg);
		return img.getAttribute("src").replaceFirst("t=s", "t=l");

	}

	public String getCurrentConvName() {

		WebElement chatHeader = driver.findElement(By.cssSelector(".pane-header.pane-chat-header"));
		WebElement title = chatHeader.findElement(By.className("chat-title"));
		WebElement titleText = title.findElement(By.cssSelector(".emojitext.ellipsify"));
		return titleText.getAttribute("title");

	}

	public void sendMsg(String msg) {
		try {
			Robot robot = new Robot();

			WebElement input = driver.findElement(By.className("input-container"));
			input.click();
			
			StringSelection stringSelection = new StringSelection(msg);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, stringSelection);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {
			e.printStackTrace();
		}

	}
	
	public List<WebElement> getVisableMessages() {
		List<WebElement> messagesWithHeader = driver.findElements(By.cssSelector(".has-author"));
		List<WebElement> headers = new ArrayList<WebElement>();
		
		for (WebElement msg : messagesWithHeader) {
			WebElement header = msg.findElement(By.cssSelector(".message-author")).findElement(By.cssSelector(".text-clickable"));
			headers.add(header);
		}
		
		return headers;
	}
	
	public WebElement getNewMessageChat() {
		try {
			WebElement newMsgNotification = driver.findElement(By.cssSelector(".icon-meta.unread-count"));
			WebElement chatImg = newMsgNotification.findElement(By.xpath("./../../../../../../.."));	
			return chatImg;
		} catch (Exception e) {
			System.out.println("No new messages.");
			return null;
		}

	}

}
