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


class ComputeServiceTest extends Specification {

    @Subject ComputeService service

    @Shared Project projectWithoutId = Stub() { getId() >> null }
    @Shared Project projectWithId = Stub() { getId() >> 'id' }
    @Shared Computation computation = new Computation(new ObjectAfm())

    void setup() {
        service = new ComputeService(Mock(RestTemplate), new GoodDataSettings())
    }

    def "execute should throw for invalid arguments"() {
        when:
        service.compute(project, afmTransformation)

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
