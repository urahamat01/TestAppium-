package boot;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BehaviorManager {
	Socket socket;
	WhatsAppBehaviors behavior;

	public BehaviorManager() {
		socket = null;
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

				public void call(Object... args) {
					System.out.println("Connected!");
					socket.emit("agentConnectedToServer");
				}

			}).on("event", new Emitter.Listener() {

				public void call(Object... args) {
					JSONObject data = new JSONObject(args[0]);
					System.out.println(data.toString());
				}

			}).on("start", new Emitter.Listener() {

				public void call(Object... args) {
					System.out.println("Starting behavior.");
					socket.emit("agentRunningToServer");
					behavior.start();
					
				}

			}).on("stop", new Emitter.Listener() {

				public void call(Object... args) {
					System.out.println("Stopping behavior.");
					socket.emit("agentStoppedToServer");
					behavior.stop();
					
				}

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

				public void call(Object... args) {
					System.out.println("Disconnected!");
					socket.emit("agentDisconnectedToServer");
				}

			});

		socket.connect();
		behavior = new SingleConversationBehavior(socket);
		
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
}
