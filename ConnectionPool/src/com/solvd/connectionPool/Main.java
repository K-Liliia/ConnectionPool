package com.solvd.connectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;

public class Main {
	// private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws SQLException, InterruptedException, ExecutionException {

		MyPool myPool = new MyPool("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://52.59.193.212:3306/Railway_station?serverTimezone=Europe/Kiev&useSSL=false", "root",
				"devintern");
		myPool.getConnection();
		myPool.closeAllConnections();

//parallel thread, don't return result - <Void> with runAsync() method
		CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {

			List<Connection> connections = new ArrayList<>();

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						connections.add(myPool.getConnection());
					} catch (InterruptedException e) {
						// logger.info("InterruptedException");
						System.out.println("InterruptedException");
					} catch (SQLException e) {
						// logger.info("SQLException");
						System.out.println("SQLException");
					}
				}
			}
		});

		future.get(); // blocks the current thread until the result is ready

	}
}
