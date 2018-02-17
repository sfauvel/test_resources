package org.sfvl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Simple webapp to test Selenium
 */
public class SimpleWebApp {

	private int port;
	private HttpServer server;

	public SimpleWebApp(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("server started at " + port);
		server.createContext("/", new RootHandler());
		server.createContext("/welcome", new HtmlHandler(Paths.get("src/test/resources/webapp"), "welcome.html"));
		server.setExecutor(null);
		server.start();
	}

	public void stop() {
		server.stop(0);
	}

	
	public class HtmlHandler implements HttpHandler {
		private final String htmlFileName;
		private final Path webPath;

		public HtmlHandler(Path webPath, String htmlFileName) {
			this.webPath = webPath;
			this.htmlFileName = htmlFileName;
		}

		@Override
		public void handle(HttpExchange he) throws IOException {
			String response = getResponse(htmlFileName);
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		private String getResponse(String htmlFileName) {
			File file = webPath.resolve(htmlFileName).toFile();
		
			String content = "";
			try {
				byte[] b = Files.readAllBytes(file.toPath());
				content = new String(b);
			} catch (NoSuchFileException e) {
				e.printStackTrace();
				return "File not found:" + file.getAbsolutePath();
			} catch (IOException e) {;
				e.printStackTrace();
				return e.getMessage();
			}
			return content;
		}

	}
	
	public class RootHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			String response = "<h1>Server start success if you see this message</h1>" + "<h1>Port: " + port + "</h1>";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}
