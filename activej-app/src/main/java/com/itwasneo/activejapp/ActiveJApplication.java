package com.itwasneo.activejapp;

import com.itwasneo.activejapp.model.Human;
import com.itwasneo.activejapp.provider.MongoProvider;
import com.itwasneo.activejapp.provider.RedisProvider;
import com.itwasneo.activejapp.repository.MongoRepository;
import com.itwasneo.activejapp.repository.RedisRepository;
import io.activej.http.AsyncServlet;
import io.activej.http.HttpMethod;
import io.activej.http.HttpResponse;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.launcher.Launcher;
import io.activej.launchers.http.HttpServerLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveJApplication extends HttpServerLauncher {

	private static final Logger logger = LoggerFactory.getLogger(ActiveJApplication.class);

	@Provides
	AsyncServlet servlet() {

		RedisRepository<Human> rr = RedisProvider.getRedisRepository();
		MongoRepository<Human> mr = MongoProvider.getMongoRepository("db-1", "human");

		return RoutingServlet.create()
				.map(HttpMethod.GET, "/hi", req -> {
					return HttpResponse.ok200().withPlainText("Hi");
				})
				.map(HttpMethod.POST, "/human", req -> {
					return HttpResponse.ok200().withPlainText("Created");
				});
	}

	public static void main(String[] args) throws Exception {
		Launcher launcher = new ActiveJApplication();
		launcher.launch(args);
	}
}
