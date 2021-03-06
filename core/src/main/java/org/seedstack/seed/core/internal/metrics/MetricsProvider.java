/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal.metrics;

import javax.inject.Singleton;

import org.seedstack.seed.core.spi.dependency.DependencyProvider;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

/**
 * Provider used to get a {@link MetricRegistry} to register an internal {@link Metric}.
 * @author thierry.bouvet@mpsa.com
 *
 */
@Singleton
public class MetricsProvider implements DependencyProvider {

	private MetricRegistry metricRegistry ;

	/**
	 * @return a {@link MetricRegistry}.
	 */
	public MetricRegistry getMetricRegistry() {
		if ( this.metricRegistry == null)
			this.metricRegistry = new MetricRegistry();
		return this.metricRegistry;
	}

	/**
	 * Register a new {@link Metric}.
	 * @param name {@link Metric} name to register.
	 * @param metricHandler {@link MetricHandler} to handle to add a new {@link Metric}.
	 */
	public void register(String name, MetricHandler metricHandler) {
		getMetricRegistry().register(name, metricHandler.handle());
	}

	/**
	 * Register a new {@link Metric}.
	 * @param name {@link Metric} name to register.
	 * @param metric {@link Metric} to register.
	 */
	public void register(String name, Metric metric) {
		getMetricRegistry().register(name, metric);
	}

	@Override
	public String getClassToCheck() {
		return "com.codahale.metrics.MetricRegistry";
	}

}
