/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.fest.reflect.core.Reflection;
import org.junit.Before;
import org.junit.Test;
import org.seedstack.seed.core.api.Install;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.core.spi.dependency.DependencyProvider;
import org.seedstack.seed.core.spi.dependency.Maybe;
import org.seedstack.seed.core.spi.diagnostic.DiagnosticInfoCollector;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import io.nuun.kernel.api.plugin.context.InitContext;
import mockit.Expectations;
import mockit.Mocked;

/**
 * CorePlugin unit test
 *
 * @author redouane.loulou@ext.mpsa.com
 */
public class CorePluginTest {
    public static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
        }
    }

    CorePlugin pluginUnderTest;

    @Before
    public void before() {
        pluginUnderTest = new CorePlugin();
    }

    @Test
    public void verify_nativeUnitModule_instance() {
        Object object = pluginUnderTest.nativeUnitModule();
        Assertions.assertThat(object).isNotNull();
        Assertions.assertThat(object).isInstanceOf(Module.class);
    }

    @Test
    public void initCorePluginTest() {
        InitContext initContext = mockInitContextForCore(TestModule.class, null, null);
        pluginUnderTest.init(initContext);
        Object object = pluginUnderTest.nativeUnitModule();
        Assertions.assertThat(object).isNotNull();
        Assertions.assertThat(object).isInstanceOf(Module.class);
        @SuppressWarnings("unchecked")
		Set<Class<? extends Module>> seedModules = Reflection.field("seedModules").ofType(Set.class).in(pluginUnderTest).get();
        Assertions.assertThat(seedModules).hasSize(1);
    }

    @Test
    public void initCorePluginTest2() {
        InitContext initContext = mockInitContextForCore(Object.class, null,null);
        pluginUnderTest.init(initContext);
        Object object = pluginUnderTest.nativeUnitModule();
        Assertions.assertThat(object).isNotNull();
        Assertions.assertThat(object).isInstanceOf(Module.class);
        @SuppressWarnings("unchecked")
		Set<Class<? extends Module>> seedModules = Reflection.field("seedModules").ofType(Set.class).in(pluginUnderTest).get();
        Assertions.assertThat(seedModules).hasSize(0);
    }

    @Test
    public void pluginPackageRootTest() {
        Assertions.assertThat(pluginUnderTest.pluginPackageRoot()).isNotNull();
    }

    @Test
    public void pluginPropertiesPrefixTest() {
        Assertions.assertThat(pluginUnderTest.pluginPropertiesPrefix()).isNotNull();
    }

    @Test
    public void classpathScanRequestsTest() {
        Assertions.assertThat(pluginUnderTest.classpathScanRequests()).hasSize(3);
    }

    @Test
    public void nameTest() {
        Assertions.assertThat(pluginUnderTest.name()).isNotNull();
    }

    private InitContext mockInitContextForCore(Class<?> moduleClass, Class<?> diagnosticClass, Class<?> dependencyClass) {
        InitContext initContext = mock(InitContext.class);

        Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass = new HashMap<Class<? extends Annotation>, Collection<Class<?>>>();
        Collection<Class<?>> classs = new ArrayList<Class<?>>();
        if (moduleClass != null) {
            classs.add(moduleClass);
        }
        scannedClassesByAnnotationClass.put(Install.class, classs);

        Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass = new HashMap<Class<?>, Collection<Class<?>>>();
        Collection<Class<?>> classs2 = new ArrayList<Class<?>>();
        if (diagnosticClass != null) {
            classs.add(diagnosticClass);
        }
        scannedSubTypesByParentClass.put(DiagnosticInfoCollector.class, classs2);
        Collection<Class<?>> providerClasses = new ArrayList<Class<?>>();
        if (dependencyClass != null) {
        	providerClasses.add(dependencyClass);
        }
        scannedSubTypesByParentClass.put(DependencyProvider.class, providerClasses);

        when(initContext.scannedClassesByAnnotationClass()).thenReturn(scannedClassesByAnnotationClass);
        when(initContext.scannedSubTypesByParentClass()).thenReturn(scannedSubTypesByParentClass);

        return initContext;
    }

    @Test
    public void load_seed_bootstrap_properties() {
        Configuration bootstrapConfiguration = pluginUnderTest.getBootstrapConfiguration();
        Assertions.assertThat(bootstrapConfiguration).isNotNull();
        Assertions.assertThat(bootstrapConfiguration.getString("package-roots")).isNotEmpty();
    }

    @Test
    public void package_root_should_valid() {
        String pluginPackageRoot = pluginUnderTest.pluginPackageRoot();
        Assertions.assertThat(pluginPackageRoot).contains(CorePlugin.SEED_PACKAGE_ROOT);
        Assertions.assertThat(pluginPackageRoot).contains("some.other.pkg");
    }
    
    @Test
    public void checkOptionalDependency(@Mocked final DependencyProvider myProvider){
    	new Expectations() {
    		{
    			myProvider.getClassToCheck();
    			result="java.lang.String";
    		}
    	};
        InitContext initContext = mockInitContextForCore(Object.class, null,myProvider.getClass());
        pluginUnderTest.init(initContext);
        Maybe<?> maybe = pluginUnderTest.getDependency(myProvider.getClass());
        Assertions.assertThat(maybe).isNotNull();
        Assertions.assertThat(maybe.isPresent()).isTrue();
    }
    
    @Test
    public void checkOptionalDependencyNOK(@Mocked final DependencyProvider myProvider){
    	new Expectations() {
    		{
    			myProvider.getClassToCheck();
    			result="xxxxx";
    		}
    	};
        InitContext initContext = mockInitContextForCore(Object.class, null,myProvider.getClass());
        pluginUnderTest.init(initContext);
        Maybe<?> maybe = pluginUnderTest.getDependency(myProvider.getClass());
        Assertions.assertThat(maybe).isNotNull();
        Assertions.assertThat(maybe.isPresent()).isFalse();
    }
    
    @Test(expected=SeedException.class)
    public void checkOptionalDependencyWithInstantiationError(){
        InitContext initContext = mockInitContextForCore(Object.class, null,DependencyProvider.class);
        pluginUnderTest.init(initContext);
    }
    
}
