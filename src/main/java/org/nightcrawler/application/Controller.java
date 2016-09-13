package org.nightcrawler.application;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;

import com.google.common.base.Preconditions;

public class Controller {

	private final Supplier<Crawler> crawlerFactory;

	public Controller(final Supplier<Crawler> crawlerFactory) {
		this.crawlerFactory = Preconditions.checkNotNull(crawlerFactory);
	}

	public String generatePageMap(final String[] args) throws ValidationException {	
		if (args.length < 1) {throw new ValidationException();}	
		try {			
			return generatePageMap(new URL(Optional.ofNullable(args[0]).orElseThrow(ValidationException::new)));
		} catch (final MalformedURLException e) {
			throw new ValidationException(e);
		}		
	}

	private String generatePageMap(final URL uri) {
		Set<Page> pages;
		pages = crawlerFactory.get().crawl(uri);
		return render(pages);
	}

	private String render(final Set<Page> pages) {
		final JsonObjectBuilder value = Json.createObjectBuilder();
		final Set<Consumer<JsonObjectBuilder>> jsonPages = pages.parallelStream()
				.map(page -> page.render(new JsonAppenderRenderer())).collect(Collectors.toSet());
		jsonPages.forEach(p -> p.accept(value));
		return value.build().toString();
	}
}
