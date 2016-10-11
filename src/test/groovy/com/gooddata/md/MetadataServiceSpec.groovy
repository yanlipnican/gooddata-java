package com.gooddata.md

import com.gooddata.project.Project
import org.springframework.web.client.RestTemplate
import org.testng.annotations.Test
import spock.lang.Specification
import spock.lang.Subject

import static org.mockito.Mockito.mock

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
    }

//    @Test(expectedExceptions = ObjCreateException.class)
//    @SuppressWarnings("unchecked")
//    public void testCreateObjGDRestException() throws Exception {
//        final Obj obj = mock(Obj.class);
//        when(restTemplate.postForObject(Obj.URI, obj, UriResponse.class, PROJECT_ID))
//                .thenThrow(GoodDataRestException.class);
//        service.createObj(project, obj);
//    }
//
//    @Test(expectedExceptions = ObjCreateException.class)
//    public void testCreateObjRestClientException() throws Exception {
//        final Obj obj = mock(Obj.class);
//        when(restTemplate.postForObject(Obj.URI, obj, UriResponse.class, PROJECT_ID))
//                .thenThrow(new RestClientException(""));
//        service.createObj(project, obj);
//    }
}
