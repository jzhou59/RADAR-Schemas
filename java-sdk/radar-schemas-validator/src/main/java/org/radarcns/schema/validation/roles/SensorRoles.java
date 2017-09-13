/*
 * Copyright 2017 King's College London and The Hyve
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.radarcns.schema.validation.roles;

import org.radarcns.catalogue.ProcessingState;
import org.radarcns.catalogue.SensorName;
import org.radarcns.schema.specification.source.passive.Sensor;

import static org.radarcns.schema.validation.roles.PassiveSourceRoles.RADAR_PROVIDERS;
import static org.radarcns.schema.validation.roles.Validator.validateNonNull;
import static org.radarcns.schema.validation.roles.Validator.validateOrNull;

/**
 * TODO.
 */
public final class SensorRoles {
    private static final String APP_PROVIDER =
            "App provider must be equal to one of the following: " + RADAR_PROVIDERS +  ".";
    private static final String DATA_TYPE =
            "Sensor data type cannot be null and should differ from "
            + ProcessingState.UNKNOWN.name() + ".";
    private static final String NAME =
            "Sensor name cannot be not null and should different from "
            + SensorName.UNKNOWN.name() + ".";

    private SensorRoles() {
        // utility class
    }

    /**
     * TODO.
     * @return TODO
     */
    static Validator<Sensor> validateAppProvider() {
        return validateOrNull(Sensor::getAppProvider, RADAR_PROVIDERS::contains, APP_PROVIDER);
    }

    /**
     * TODO.
     * @return TODO
     */
    static Validator<Sensor> validateDataType() {
        return validateNonNull(Sensor::getProcessingState,
            state -> !state.name().equals(ProcessingState.UNKNOWN.name()), DATA_TYPE);
    }

    /**
     * TODO.
     * @return TODO
     */
    static Validator<Sensor> validateName() {
        return validateNonNull(Sensor::getName,
                name -> name.name().equals(SensorName.UNKNOWN.name()), NAME);
    }
}
