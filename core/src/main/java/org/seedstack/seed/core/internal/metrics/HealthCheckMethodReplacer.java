/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal.metrics;

import org.seedstack.seed.core.utils.ProxyMethodReplacer;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;

/**
 * {@link ProxyMethodReplacer} for a {@link HealthCheck}.
 * @author thierry.bouvet@mpsa.com
 *
 */
public interface HealthCheckMethodReplacer extends ProxyMethodReplacer{

	/**
	 * Return the result of an {@link HealthCheck} test.
	 * @return the {@link Result} for the {@link HealthCheck} test. 
	 * @see HealthCheck
	 */
	Result check();
}
