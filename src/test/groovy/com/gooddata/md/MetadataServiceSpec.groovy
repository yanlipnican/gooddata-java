package com.gooddata.md

import com.gooddata.project.Project
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
}
