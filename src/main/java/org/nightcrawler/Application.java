package org.nightcrawler;

import org.nightcrawler.application.Controller;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try(final AnnotationConfigApplicationContext ctx = startContext()) {
			final Controller controller = ctx.getBean(Controller.class);
			System.out.print(controller.generatePageMap(args));
		}					
	}

	private static AnnotationConfigApplicationContext startContext() {
		final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(CrawlerConfig.class);
		ctx.refresh();
		return ctx;
	}

}