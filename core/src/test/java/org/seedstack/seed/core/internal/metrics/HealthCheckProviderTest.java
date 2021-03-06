/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal.metrics;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;

public class HealthCheckProviderTest {

	@Test
	public void testGetClassToCheck() {
		HealthcheckProvider healthCheckProvider = new HealthcheckProvider();
		Assertions.assertThat(healthCheckProvider.getClassToCheck()).isNotEmpty();
	}

	@Test
	public void testGetHealthCheckRegistry() {
		HealthcheckProvider healthCheckProvider = new HealthcheckProvider();
		HealthCheckRegistry registry = healthCheckProvider.getHealthCheckRegistry();
		Assertions.assertThat(registry).isNotNull();
		Assertions.assertThat(healthCheckProvider.getHealthCheckRegistry()).isEqualTo(registry);
	}

	@Test
	public void testRegisterStringHealthCheck() {
		HealthcheckProvider healthCheckProvider = new HealthcheckProvider();
		HealthCheck healthCheck = new HealthCheck(){

			@Override
			protected Result check() throws Exception {
				return null;
			}
		};
		final String name = "name";
		healthCheckProvider.register(name, healthCheck);
		Assertions.assertThat(healthCheckProvider.getHealthCheckRegistry().getNames().contains(name)).isTrue();
	}

	@Test
	public void testRegisterStringHealthCheckMethodReplacer() {
		HealthcheckProvider healthCheckProvider = new HealthcheckProvider();
		final String name = "name";
		healthCheckProvider.register(name, new HealthCheckMethodReplacer() {
			
			@Override
			public Result check() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		Assertions.assertThat(healthCheckProvider.getHealthCheckRegistry().getNames().contains(name)).isTrue();
	}

}
