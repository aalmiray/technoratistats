import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.auth.*

class HttpHelper {
    HttpClient httpClient = new HttpClient()

    def post(url, data, parse = true) {
        def postData = []
        for (pair in data.keySet()) {
            def nameValuePair = new NameValuePair(pair, String.valueOf(data[pair]))
            postData += nameValuePair
        }

        PostMethod post = null
        try {
            post = new PostMethod(url)
            post.setRequestBody(postData as NameValuePair[])
            httpClient.executeMethod(post)
            def result = post.getResponseBodyAsStream().text
            if (parse) {
                return new XmlSlurper().parseText(result)
            }
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            post.releaseConnection()
        }
        return null
    }

    def get(url) {
        def data = url.toURL().text
        return new XmlSlurper().parseText(data)
    }
}