package com.itwasneo.activejapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwasneo.activejapp.model.Human;
import com.itwasneo.activejapp.provider.MongoProvider;
import com.itwasneo.activejapp.provider.RedisProvider;
import com.itwasneo.activejapp.repository.MongoRepository;
import com.itwasneo.activejapp.repository.RedisRepository;
import io.activej.bytebuf.ByteBuf;
import io.activej.http.AsyncServlet;
import io.activej.http.HttpMethod;
import io.activej.http.HttpResponse;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.launcher.Launcher;
import io.activej.launchers.http.HttpServerLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class ActiveJApplication extends HttpServerLauncher {

	private static final Logger logger = LoggerFactory.getLogger(ActiveJApplication.class);

	@Provides
	AsyncServlet servlet() {

		RedisRepository<Human> rr = RedisProvider.getRedisRepository();
		MongoRepository<Human> mr = MongoProvider.getMongoRepository("db-1", "human");
		ObjectMapper om = RedisProvider.getObjectMapper();

		return RoutingServlet.create()
				.map(HttpMethod.GET, "/human/:key", req ->
						rr.findByKeys(Human.class, req.getPathParameter("key"))
								.map(s -> HttpResponse.ok200().withJson(s))
								.orElseGet(() -> HttpResponse.ok200()
										.withPlainText("Not found")))
				.map(HttpMethod.POST, "/human", req -> req.loadBody()
						.map(promise -> {
							ByteBuf body = req.getBody();
							try {
								byte[] bodyBytes = body.getArray();
								Human human = om.readValue(bodyBytes, Human.class);
								return HttpResponse.ok200()
										.withPlainText(String.valueOf(rr.save(human, human.name())));
							} catch (IOException e) {
								return HttpResponse.ofCode(400);
							}
				}));
	}

	public static void main(String[] args) throws Exception {
		Launcher launcher = new ActiveJApplication();
		launcher.launch(args);
	}
}
