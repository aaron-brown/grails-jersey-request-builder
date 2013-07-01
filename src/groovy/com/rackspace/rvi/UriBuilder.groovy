package com.rackspace.rvi

import javax.ws.rs.core.UriBuilder as JsrUriBuilder
import org.apache.log4j.Logger

/**
 * Convenience wrapper around JSR311's UriBuilder that provides
 * a groovy-like builder syntax.
 *
 * The class can be used in 3 ways:
 *
 * Builder-style
 * -------------
 * <code>
 * URI uri = UriBuilder.build {
 *     host = 'www.example.com'
 *     path = ['resource', someVariable, 'view']
 *     port = 80
 *     scheme = 'http'
 * }
 * </code>
 *
 * Object-style
 * ------------
 * <code>
 * UriBuilder builder = new UriBuilder()
 * builder.host = 'www.example.com'
 * builder.path = ['resource', someVariable, 'view']
 * builder.port = 80
 * builder.scheme = 'http'
 * Uri uri = builder.build()
 * </code>
 *
 * Template-style
 * --------------
 * <code>
 * Uri uri = UriBuilder.build('http://www.example.com/resource/{var1}/view', someVariable)
 * </code>
 *
 * Notes
 * -----
 * The class has a propery named <code>base</code>.  When set via the builder syntax or
 * object syntax, the uri will be parsed first, and then any other properties will override
 * those values parsed from the uri, except for path which will add to it.  As an example:
 *
 * <code>
 * URI uri = UriBuilder.build {
 *     base = 'https://www.example.com:8080/old/path'
 *     host = 'foo.bar'
 *     path = ['new', 'path']
 *     scheme = 'http'
 *     port = 8081
 * }
 * </code>
 * This code will result in the URI: <code>http://foo.bar:8081/old/path/new/path</code>.
 */
class UriBuilder {
    /**
     * Logger
     */
    private Logger log = Logger.getLogger(getClass().name)

    /**
     * Base URL
     */
    String base = null

    /**
     * Host name
     */
    String host = null

    /**
     * Port number (default 80)
     */
    Integer port = null

    /**
     * Path parts
     */
    List path = []

    /**
     * Protocol (default "http")
     */
    String scheme = null

    /**
     * URL fragment
     */
    String fragment = null

    /**
     * Creates a URI via a builder syntax.
     *
     * @param closure
     * @return
     */
    public static URI build(Closure closure) {
        // Create the builder
        UriBuilder builder = new UriBuilder()

        // Run the closure
        builder.run(closure)

        // Build and return a URI object
        return builder.build()
    }

    /**
     * Convenience function that builds a URI based on a path template.
     *
     * Example:
     * <code>UriBuilder.build('http://www.example.com/api/resource/{var1}/{var2}', variable1, variable2)</code>
     *
     * @param template
     * @param pieces
     * @return
     */
    public static URI build(String template, String... pieces) {
        // Run through as a template
        return JsrUriBuilder.fromPath(template).build(pieces)
    }

    /**
     * Runs a passed closure to implement builder-style operation.
     *
     * @param closure
     */
    private void run(Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.OWNER_FIRST
        closure.call()
    }

    /**
     * Does the work of building the URI.
     *
     * @return
     */
    public URI build() {
        // If a base URL is set, parse it out
        if (base) {
            // Parse the URL
            URI uri = new URI(base)

            // Clean up the paths
            List pathParts = uri.path.split('/')
            pathParts.removeAll([''])

            // Replace parts if not set
            host = host ?: uri.host
            port = port ?: uri.port
            scheme = scheme ?: uri.scheme
            fragment = fragment ?: uri.fragment
            path = pathParts + path
        }

        // The host is absolutely required
        if (!host) {
            throw new IllegalArgumentException('host name is required')
        }

        // Create the builder
        JsrUriBuilder builder = JsrUriBuilder.newInstance()

        // Determine the scheme
        scheme = scheme ?: 'http'

        // Determine the port
        if (!port) {
            port = (scheme == 'https') ? 443 : 80
        }

        // Set the port
        if ((scheme != 'http' || port != 80) && (scheme != 'https' || port != 443)) {
            builder.port(port)
        }

        // Set the scheme
        builder.scheme(scheme)

        // Set the host
        builder.host(host)

        // Set the fragment
        builder.fragment(fragment)

        // Add path parts
        path.each {
            builder.path(it as String)
        }

        return builder.build()
    }
}
