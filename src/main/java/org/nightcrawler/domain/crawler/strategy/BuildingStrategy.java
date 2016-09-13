package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

class BuildingStrategy<P> extends HandlingStrategy {
	private final PageBuilder<P> builder;
	private final Consumer<P> handler;
	private final HandlingStrategy base;

	BuildingStrategy(HandlingStrategy base, PageBuilder<P> builder, Consumer<P> handler) {
		this.builder = builder;
		this.handler = handler;
		this.base = base;
	}

	@Override
	public <T> T forAddress(final Function<URI, T> transformation) {
		return base.forAddress(transformation);
	}

	public HandlingStrategy link(final URI link) {
		base.link(link);
		builder.link(link);
		return this;
	}

	public HandlingStrategy resource(final URI resource) {
		base.resource(resource);
		builder.resource(resource);
		return this;
	}

	public void process() {
		final URI uri = base.forAddress(Function.identity());
		handler.accept(builder.build(uri));
		base.process();
	}
}