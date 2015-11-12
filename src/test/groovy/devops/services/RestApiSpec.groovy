package devops.services

import groovyx.net.http.AuthConfig
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.apache.http.HttpHeaders
import org.apache.http.entity.BasicHttpEntity
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class RestApiSpec extends Specification {
    RestApi restApi

    def setup() { restApi = new RestApi() }

    def cleanup() { restApi = null }

    def 'Configure and execute a request'() {
        setup:
        restApi.with {
            httpMethod = 'post'
            uri = 'bob.com'
            username = 'username'
            password = 'password'
            requestContentType = 'requestContentType'
            requestBody = 'requestBody'
            contentType = 'contentType'
        }

        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockAuth = Mock(AuthConfig)

        def mockResponse = Mock(HttpResponseDecorator)

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.getAuth() >> { mockAuth }
        1 * mockAuth.basic('username', 'password')
        1 * mockClient.post(_ as Map) >> { Map params ->
            assert params.body == 'requestBody'
            assert params.contentType == 'contentType'
            assert params.requestContentType == 'requestContentType'
            mockResponse
        }
    }

    def 'Configure and execute a preemptive authentication request'() {
        setup:
        restApi.with {
            httpMethod = 'post'
            uri = 'bob.com'
            preemptiveAuth = true
            username = 'username'
            password = 'password'
            requestContentType = 'requestContentType'
            requestBody = 'requestBody'
            contentType = 'contentType'
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockAuth = Mock(AuthConfig)

        def mockResponse = Mock(HttpResponseDecorator)

        def headers = [:]

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.getHeaders() >> { headers }
        1 * mockClient.getAuth() >> { mockAuth }
        1 * mockAuth.basic('username', 'password')
        1 * mockClient.post(_ as Map) >> { Map params ->
            assert params.body == 'requestBody'
            assert params.contentType == 'contentType'
            assert params.requestContentType == 'requestContentType'
            assert headers.get(HttpHeaders.AUTHORIZATION) == 'Basic dXNlcm5hbWU6cGFzc3dvcmQ='
            mockResponse
        }
    }

    def 'Configure and execute a request with a custom header'() {
        setup:
        restApi.with {
            httpMethod = 'post'
            uri = 'bob.com'
            username = 'username'
            password = 'password'
            requestContentType = 'requestContentType'
            requestBody = 'requestBody'
            contentType = 'contentType'
            requestHeaders = ['key': 'value']
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockAuth = Mock(AuthConfig)

        def mockResponse = Mock(HttpResponseDecorator)

        def headers = [:]

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.getAuth() >> { mockAuth }
        1 * mockClient.getHeaders() >> { headers }
        1 * mockAuth.basic('username', 'password')
        1 * mockClient.post(_ as Map) >> { Map params ->
            assert headers.get('key') == 'value'
            mockResponse
        }
    }

    def 'Configure and execute a request using a proxy'() {
        setup:
        System.setProperty("${protocol}.proxyHost", 'www.abc.com')
        System.setProperty("${protocol}.proxyPort", port.toString())
        restApi.with {
            httpMethod = 'post'
            uri = 'bob.com'
            requestContentType = 'requestContentType'
            requestBody = 'requestBody'
            contentType = 'contentType'
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockResponse = Mock(HttpResponseDecorator)

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.setProxy('www.abc.com', port, protocol)
        1 * mockClient.post(_ as Map) >> { Map params ->
            mockResponse
        }

        where:
        protocol | port
        'http'   | 8080
        'https'  | 8443
    }

    def 'Configure and execute a request with a custom string response handler'() {
        setup:
        def responseCalled = false
        restApi.with {
            uri = 'bob.com'
            contentType = 'contentType'
            responseHandler = { String responseText ->
                responseCalled = (responseText == 'called')
            }
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockResponse = Mock(HttpResponseDecorator) {
            getEntity() >> {
                def entity = new BasicHttpEntity()
                entity.content = new ByteArrayInputStream('called'.getBytes())
                entity
            }
        }

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.get(_ as Map) >> { Map params ->
            assert params.contentType == 'contentType'
            mockResponse
        }
        responseCalled
    }

    def 'Configure and execute a request with a custom input stream response handler'() {
        setup:
        def responseCalled = false
        restApi.with {
            uri = 'bob.com'
            contentType = 'contentType'
            responseHandler = { InputStream is ->
                responseCalled = (is.text == 'called')
            }
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockResponse = Mock(HttpResponseDecorator) {
            getEntity() >> {
                def entity = new BasicHttpEntity()
                entity.content = new ByteArrayInputStream('called'.getBytes())
                entity
            }
        }

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.get(_ as Map) >> { Map params ->
            assert params.contentType == 'contentType'
            mockResponse
        }
        responseCalled
    }

    def 'Configure and execute a request with a custom data response handler'() {
        setup:
        def responseCalled = false
        restApi.with {
            uri = 'bob.com'
            contentType = 'contentType'
            responseHandler = { Map map ->
                responseCalled = (map.content == 'called')
            }
        }
        def mockClient = Mock(RESTClient)
        restApi.client = mockClient

        def mockResponse = Mock(HttpResponseDecorator) {
            getData() >> {
                [content: 'called']
            }
        }

        when:
        restApi.executeRequest()

        then:
        1 * mockClient.setUri('bob.com')
        1 * mockClient.get(_ as Map) >> { Map params ->
            assert params.contentType == 'contentType'
            mockResponse
        }
        responseCalled
    }
}
