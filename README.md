Jersey Client RequestBuilder Plugin
===================================

Changelog
---------
**1.0.13**
* Added Jersey Server as a dependency of the plugin. It's not actually required for
  the plugin to work, however, when an application shares a tomcat server that _is_
  using the server library, issues occur that require server to be added to the
  project.

**1.0.12**
* Added basic HTTP auth support.

**1.0.11**
* Made request builder object stateful for reuse.
* Added connection and read timeouts.

**1.0.10**
* Added logging using log4j of the request and response. See the "debug" option.

**1.0.9**
* Fix another silly bug related to JSON conversion.
* Added lists to the JSON conversion process.

**1.0.8**
* Fix order of operations problem with JSON conversion.

**1.0.7**
* Also set the content-type for map to JSON conversions.

**1.0.6**
* Did a check for maps in the body of a request, and convert them to JSON if maps are found.

**1.0.5**
* Added cookie support.
* Changed closure delegation mode to owner first, should make your code work more intuitively.
* Added sub-type exceptions for http status codes.  All inherit from ResponseStatusException.

**1.0.4**
* Added SSL cleanup code, fixes a bug where SSL certs are always ignored.

**1.0.3**
* Added ability to ignore invalid certs.

**1.0.2**
* Added content-type.

**1.0.1**
* Initial release.
