/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm

import com.gooddata.GoodDataSettings
import com.gooddata.project.Project
import com.gooddata.executeafm.afm.ObjectAfm
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject


class ExecuteAfmServiceTest extends Specification {

    @Subject ExecuteAfmService service

    @Shared Project projectWithoutId = Stub() { getId() >> null }
    @Shared Project projectWithId = Stub() { getId() >> 'id' }
    @Shared Execution computation = new Execution(new ObjectAfm())

    void setup() {
        service = new ExecuteAfmService(Mock(RestTemplate), new GoodDataSettings())
    }

    def "execute should throw for invalid arguments"() {
        when:
        service.executeAfm(project, afmTransformation)

        then:
        thrown(IllegalArgumentException)

        where:
        project          | afmTransformation
        null             | null
        null             | computation
        projectWithoutId | null
        projectWithoutId | computation
        projectWithId    | null
    }
}
