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

package org.radarbase.schema.validation;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radarbase.schema.SchemaCatalogue;
import org.radarbase.schema.Scope;
import org.radarbase.schema.specification.SourceCatalogue;
import org.radarbase.schema.specification.config.SchemaConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.radarbase.schema.Scope.ACTIVE;
import static org.radarbase.schema.Scope.CATALOGUE;
import static org.radarbase.schema.Scope.CONNECTOR;
import static org.radarbase.schema.Scope.KAFKA;
import static org.radarbase.schema.Scope.MONITOR;
import static org.radarbase.schema.Scope.PASSIVE;
import static org.radarbase.schema.validation.ValidationHelper.COMMONS_PATH;

/**
 * TODO.
 */
public class SchemaValidatorTest {
    private SchemaValidator validator;
    private static final Path ROOT = Paths.get("../..").toAbsolutePath().normalize();
    private static final Path COMMONS_ROOT = ROOT.resolve(COMMONS_PATH);

    @BeforeEach
    public void setUp() {
        SchemaConfig config = new SchemaConfig();
        validator = new SchemaValidator(COMMONS_ROOT, config);
    }

    @Test
    public void active() throws IOException {
        testScope(ACTIVE);
    }

    @Test
    public void activeSpecifications() throws IOException {
        testFromSpecification(ACTIVE);
    }

    @Test
    public void monitor() throws IOException {
        testScope(MONITOR);
    }

    @Test
    public void monitorSpecifications() throws IOException {
        testFromSpecification(MONITOR);
    }

    @Test
    public void passive() throws IOException {
        testScope(PASSIVE);
    }

    @Test
    public void passiveSpecifications() throws IOException {
        testFromSpecification(PASSIVE);
    }

    @Test
    public void kafka() throws IOException {
        testScope(KAFKA);
    }

    @Test
    public void kafkaSpecifications() throws IOException {
        testFromSpecification(KAFKA);
    }

    @Test
    public void catalogue() throws IOException {
        testScope(CATALOGUE);
    }

    @Test
    public void catalogueSpecifications() throws IOException {
        testFromSpecification(CATALOGUE);
    }

    @Test
    public void connectorSchemas() throws IOException {
        testScope(CONNECTOR);
    }

    @Test
    public void connectorSpecifications() throws IOException {
        testFromSpecification(CONNECTOR);
    }

    private void testFromSpecification(Scope scope) throws IOException {
        SourceCatalogue sourceCatalogue = SourceCatalogue.Companion.load(ROOT);
        String result = SchemaValidator.format(
                validator.analyseSourceCatalogue(scope, sourceCatalogue));

        if (!result.isEmpty()) {
            fail(result);
        }
    }

    private void testScope(Scope scope) throws IOException {
        SchemaCatalogue schemaCatalogue = new SchemaCatalogue(COMMONS_ROOT, new SchemaConfig(),
                scope);
        String result = SchemaValidator.format(
                validator.analyseFiles(scope, schemaCatalogue));

        if (!result.isEmpty()) {
            fail(result);
        }
    }

    @Test
    public void testEnumerator() {
        Path schemaPath =  COMMONS_ROOT.resolve(
                "monitor/application/application_server_status.avsc");

        String name = "org.radarcns.monitor.application.ApplicationServerStatus";
        String documentation = "Mock documentation.";

        Schema schema = SchemaBuilder
                .enumeration(name)
                .doc(documentation)
                .symbols("CONNECTED", "DISCONNECTED", "UNKNOWN");

        Stream<ValidationException> result = validator.validate(schema, schemaPath, MONITOR);

        assertEquals(0, result.count());

        schema = SchemaBuilder
                .enumeration(name)
                .doc(documentation)
                .symbols("CONNECTED", "DISCONNECTED", "un_known");

        result = validator.validate(schema, schemaPath, MONITOR);

        assertEquals(2, result.count());
    }
}
