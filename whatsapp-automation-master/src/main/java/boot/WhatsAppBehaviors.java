package boot;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import io.socket.client.Socket;
import utilities.Browser;
import utilities.WhatsAppDriver;

public abstract class WhatsAppBehaviors {
	//driver
	public WhatsAppDriver whatsapp;
	public Socket socket;
	public boolean running = false;
	
	public WhatsAppBehaviors(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * @author Yuval
	 * @param none
	 * <br>
	 * <b>Desctiption: </b><br>
	 * starts the driver and waits for connection
	 */
	public void start(){
		running = true;
		whatsapp = new WhatsAppDriver(Browser.CHROME);
		whatsapp.open();
		try {
			whatsapp.waitForConnection();

			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(TimeoutException e) {
			System.out.println("WhatsApp Web didn't connected. Aborting...");
			System.out.println(e);
		}
	}
	public void stop(){
		running = false;
		
		System.out.println("Stopped.");
	};
	public abstract void openConversation();
	public abstract void sendMessage();
	public abstract WebElement getAnswer();
}
