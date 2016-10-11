package com.gooddata.md

import com.gooddata.GoodDataRestException
import com.gooddata.gdc.UriResponse
import com.gooddata.project.Project
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

class MetadataServiceSpec extends Specification {

    private static final String URI = "TEST_URI"
    private static final String ID = "TEST_ID"
    private static final String PROJECT_ID = "TEST_PROJ_ID"

    Project project;
    RestTemplate restTemplate;

    @Subject MetadataService service;

    void setup() {
        restTemplate = Mock(RestTemplate)
        service = new MetadataService(restTemplate)

        project = Stub(Project) {
            getId() >> PROJECT_ID
        }
    }

    void "createObj() throws on null"() {
        when:
        service.createObj(project, Stub(Obj))

        then:
        thrown(ObjCreateException)

        and:
        1 * restTemplate.postForObject(_, _, _, _) >> null
    }

    void "createObj() should throw"() {
        given:
        def obj = Stub(Obj)
        restTemplate.postForObject(Obj.CREATE_URI, obj, UriResponse, PROJECT_ID) >> { throw ex }

        when:
        service.createObj(project, obj)

        then:
        thrown(ObjCreateException)

        where:
        ex << [new GoodDataRestException(500, 'req', '', '', ''), new RestClientException('')]
    }
}
