/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal.jndi;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.seedstack.seed.core.api.FromContext;
import org.seedstack.seed.core.api.SeedException;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Guice type listener for {@link Resource} annotated fields.
 *
 * @author adrien.lauer@mpsa.com
 */
class ResourceTypeListener implements TypeListener {
    private Map<String, Context> jndiContexts;
    private Context defaultContext;

    ResourceTypeListener(Context defaultContext, Map<String, Context> jndiContexts) {
        this.defaultContext = defaultContext;
        this.jndiContexts = jndiContexts;
    }

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass()) {
            for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
                Resource resourceAnnotation = field.getAnnotation(Resource.class);
                if (resourceAnnotation != null) {
                    Context contextToLookup = defaultContext;
                    FromContext fromContextAnnotation = field.getAnnotation(FromContext.class);
                    if (fromContextAnnotation != null) {
                        contextToLookup = jndiContexts.get(fromContextAnnotation.value());
                    }

                    String resourceName = resourceAnnotation.name();
                    if (resourceName != null && !resourceName.isEmpty()) {
                        try {
                            typeEncounter.register(new ResourceMembersInjector<T>(field, contextToLookup.lookup(resourceName)));
                        } catch (NamingException e) {
                            String contextName = "default";
                            if (fromContextAnnotation != null) {
                                contextName = fromContextAnnotation.value();
                            }

                            throw SeedException.wrap(e, JndiErrorCode.UNABLE_TO_REGISTER_INJECTION_FOR_RESOURCE)
                                    .put("field", field.getName())
                                    .put("class", c.getCanonicalName())
                                    .put("resource", resourceName)
                                    .put("context", contextName);
                        }
                    }
                }
            }
        }
    }
}
