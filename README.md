Vaadin-SpringSecurityViewProvider
=================================

This add-on has the following goals:

* Integrate Spring Security into a Vaadin application
* Make Vaadin Views Spring managed prototype beans available through / for a navigator
* Simple description of views and no redundant @Component and @Scope on every view

Prerequisites
-------------

* A working Spring 3.2 setup including load-time-weaving and Spring Security for your application (See ["Using @Configurable in Vaadin Components"][1] for more information)
* A working setup of the ["SpringVaadinIntegration Add-on"][2]
* A Vaadin appliction protected by Spring Security with a non-Vaadin login page

Protecting your appliction
--------------------------

I recommend using the following setup of the SpringSecurityFilterChain, a dispatcher servlet and the SpringVaadinServlet like this in web.xml

```
	<servlet>
	    <servlet-name>web</servlet-name>
	    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	    <load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>web</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
    <servlet>
        <servlet-name>application</servlet-name>
        <servlet-class>ru.xpoft.vaadin.SpringVaadinServlet</servlet-class>
        <init-param>
            <param-name>beanName</param-name>
            <param-value>application</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>application</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
		<servlet-name>application</servlet-name>
		<url-pattern>/VAADIN/*</url-pattern>
	</servlet-mapping>
```

Your login page can be a simple JSP page, a Spring JSTL view or whatever you like. It may not live under /app.

Examples
--------

Have a look at SpringSecurityViewProviderTest for an usage example and setting up basic security. There is also a [blog post][3] about the original idea.

License
-------

  **Apache License, Version 2.0**

  Copyright 2013 ENERKO Informatik GmbH

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
     
      http://www.apache.org/licenses/LICENSE-2.0
     
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.



  [1]: http://info.michael-simons.eu/2013/03/12/vaadin-spring-using-configurable-in-vaadin-components/
  [2]: https://vaadin.com/directory#addon/springvaadinintegration
  [3]: http://info.michael-simons.eu/2013/03/30/vaadin-spring-integrating-vaadin-with-spring-security/