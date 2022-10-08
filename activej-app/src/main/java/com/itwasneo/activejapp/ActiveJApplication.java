package com.itwasneo.activejapp;

import io.activej.http.AsyncServlet;
import io.activej.http.HttpMethod;
import io.activej.http.HttpResponse;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.launcher.Launcher;
import io.activej.launchers.http.HttpServerLauncher;

public class ActiveJApplication extends HttpServerLauncher {

	@Provides
	AsyncServlet servlet() {

		return RoutingServlet.create()
				.map(HttpMethod.GET, "/hi", req -> {
					return HttpResponse.ok200().withPlainText("Hi");
				});
	}



	public static void main(String[] args) throws Exception {
		Launcher launcher = new ActiveJApplication();
		launcher.launch(args);
	}
}
