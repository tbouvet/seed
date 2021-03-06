/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.internal.command;

import org.seedstack.seed.core.spi.command.Option;

import java.lang.reflect.Field;

/**
 * Holds the definition of a command option.
 *
 * @author adrien.lauer@mpsa.com
 */
class OptionDefinition {
    private final Option option;
    private final Field field;

    OptionDefinition(Option option, Field field) {
        this.option = option;
        this.field = field;
    }

    String getName() {
        return option.name();
    }

    String getLongName() {
        return option.longName();
    }

    boolean hasArgument() {
        return option.hasArgument();
    }

    String getDescription() {
        return option.description();
    }

    boolean isMandatory() {
        return option.mandatory();
    }

    String getDefaultValue() {
        return option.defaultValue();
    }

    Option getAnnotation() {
        return option;
    }

    Field getField() {
        return field;
    }
}
