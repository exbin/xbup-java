XBUP Java Catalog Web Application
=================================

This is experimental application with XBUP support written in Java.

Development
===========

 * NetBeans 8.0 or later

Althought project can be opened using gradle plugin, it doesn't support code completition in web pages. As alternative way, you can generate pom.xml using following command:

gradle createPom

Also copy xbup-core and xbup-catalog jar files in local maven repository.

It can be copied manually to .m2/repository/xbcatalogweb/modules/xbup-$project_name/$version in your user home directory or Local Settings folder.
To open project using pom.xml, you might need to temporary disable gradle plugin or even clear NetBeans cache.

Homepage: http://xbup.exbin.org
