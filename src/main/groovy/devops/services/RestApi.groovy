package devops.services

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.commons.lang.StringUtils
import org.apache.http.HttpHeaders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author Sion Williams
 */
@Component
class RestApi {

    private static final Logger logger = LoggerFactory.getLogger(RestApi)

    RESTClient client
    String httpMethod
    Object uri
    boolean preemptiveAuth
    String username
    String password
    Object requestContentType
    Object contentType
    Object requestBody
    Object requestHeaders
    Closure responseHandler
    HttpResponseDecorator serverResponse

    RestApi() {
        httpMethod = 'get'
        client = new RESTClient()
    }

    void configureProxy(String protocol) {
        String proxyHost = System.getProperty("${protocol}.proxyHost", '')
        int proxyPort = System.getProperty("${protocol}.proxyPort", '0') as int
        if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
            logger.info "Using ${protocol.toUpperCase()} proxy: $proxyHost:$proxyPort"
            client.setProxy(proxyHost, proxyPort, protocol)
        }
    }

    void executeRequest() {
        if (!uri || StringUtils.isBlank(uri)) {
            throw new Exception('No resource URI provided')
        }

        client.uri = uri
        if (StringUtils.isNotBlank(username)) {
            if (preemptiveAuth) {
                client.headers[HttpHeaders.AUTHORIZATION] = 'Basic ' + ("$username:$password".toString().bytes.encodeBase64())
            }
            client.auth.basic(username, password)
        }

        configureProxy('http')
        configureProxy('https')

        if (requestHeaders instanceof Map) {
            client.headers.putAll(requestHeaders);
        }

        def params = [:]
        if (requestBody) {
            params.body = requestBody
        }
        if (contentType) {
            params.contentType = contentType
        }
        if (requestContentType) {
            params.requestContentType = requestContentType
        }

        logger.info "Executing a '$httpMethod' request to '$uri'"

        try {
            serverResponse = client."${httpMethod.toLowerCase()}"(params)
            if (noResponseHandler()) {
                logger.info "Server Response:" + System.lineSeparator() + serverResponse.getData()
            } else {
                callResponseHandler()
            }
        } catch (HttpResponseException e) {
            throw new Exception(e.getResponse().getData().toString(), e)
        }
    }

    private boolean noResponseHandler() {
        !responseHandler || responseHandler.maximumNumberOfParameters != 1
    }

    void callResponseHandler() {
        def parameterType = responseHandler.parameterTypes.first()
        if (InputStream.isAssignableFrom(parameterType)) {
            responseHandler.call(serverResponse.entity.content)
        } else if (String.isAssignableFrom(parameterType)) {
            serverResponse.entity.content.withStream {
                responseHandler.call(it.text)
            }
        } else {
            responseHandler.call(serverResponse.data)
        }
    }
}
