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

import org.radarcns.schema.specification.source.active.ActiveSource;
import org.radarcns.schema.specification.source.active.questionnaire.QuestionnaireSource;

import static org.radarcns.schema.validation.roles.Validator.validateNonNull;

/**
 * TODO.
 */
public final class ActiveSourceRoles {
    private static final String ASSESSMENT_TYPE = "Assessment Type should be equal to "
            + ActiveSource.RadarSourceTypes.QUESTIONNAIRE.name() + ".";
    private static final String TOPICS = "Topic set is invalid. It should contain only the topic"
            + " specified in the configuration file.";


    private ActiveSourceRoles() {
        // utility class
    }

    /**
     * TODO.
     * @return TODO
     */
    static Validator<QuestionnaireSource> validateAssessmentType() {
        return validateNonNull(QuestionnaireSource::getQuestionnaireType, type -> type.equals(
                    ActiveSource.RadarSourceTypes.QUESTIONNAIRE.name()), ASSESSMENT_TYPE);
    }

    /**
     * TODO.
     * @return TODO
     */
    static Validator<ActiveSource> validateTopics() {
        return validateNonNull(ActiveSource::getTopics, topics -> topics.size() == 1, TOPICS);
    }
}
