Introduction
============
This quickstart demonstrates how policy can be used to control the security characteristics of a
service invocation.  The quickstart demo application has two services, one SOAP-based called "WorkService", and one REST-based called "OrderService".
Keycloak / OpenId is used for "clientAuthentication".

Pre requirements
======================
1. Install keycloak server (1.7) on port 9080  http://keycloak.jboss.org/keycloak/downloads-archive.html?dir=0%3D1.7.0.Final%3B 
2. Start Keycloak Server
3. Import the realm configuration:
	<keycloakdir>/bin/standalone.sh -Dkeycloak.migration.action=import -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=demo2-keycloak.json -Dkeycloak.migration.strategy=OVERWRITE_EXISTING
4. Install wildfly 8.1.0.Final
5. Add the switchyard modules to Wildfly and the switchyard-wildfly configuration
6. Add the keycloak-wildfly adapter to Wildfly
7. Add/Verify wildfly standalone.xml configuration:
	7.1.
	<extensions>
		...
		<extension module="org.switchyard"/>
        <extension module="org.keycloak.keycloak-adapter-subsystem"/>
		...
	</extensions>

	7.2.
	<profile>
	...
		<subsystem xmlns="urn:jboss:domain:security:1.2">
            <security-domains>
				...
				<security-domain name="openid-validate-token" cache-type="default">
                    <authentication>
                        <login-module code="org.keycloak.adapters.jaas.BearerTokenLoginModule" flag="required">
                            <module-option name="keycloak-config-file" value="classpath:META-INF/keycloak.json"/>
                            <module-option name="role-principal-class" value="org.switchyard.security.principal.RolePrincipal"/>
                        </login-module>
                    </authentication>
                </security-domain>
				...
            </security-domains>
        </subsystem>
	...
	</profile>
	
	7.3
	<profile>
	...
		<subsystem xmlns="urn:jboss:domain:switchyard:1.0">
            ...
			<security-configs/>
            <modules>
                ...
            </modules>
            <extensions>
                ...
            </extensions>
			...
        </subsystem>
	...
	</profile>
	
	7.4
	<profile>
	...
		<subsystem xmlns="urn:jboss:domain:keycloak:1.1">
            <secure-deployment name="switchyard-demo-policy-security-openid.war">
                <realm>demo2</realm>
                <resource>policy-security-openid</resource>
                <bearer-only>true</bearer-only>
                <realm-public-key>MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuuKsh2NYf/Zj7bSRNdT6RhvIA+vEtLkwmCCJmf9lSMLo45Pb7WKmApprfz81AHORDUV9+FuAB6euBq2Jk3fQgjI7ay629Ku6dMY06ESt7C4ky3mlfNFprIMpiB/AZMkQq9b1P6d0g3wUaYnEpJtmjU5gq0HcwZHbRoMI9MomDZThXYNPvi4IBXXpWasM9KtAGF8UklUCI5fl03R1hB1ssJwyZ4dQJiFJY5G0JaoqsFUrWezRimNioNH+JPt5JatLQ1oEUsx4zit4sjJJM3zWTpiBl3twRrgpW3bv+DC5zJY311N2XdHPwmjivv3oGFLT/G+2tAY8dx8OWl8V3IIkpwIDAQAB</realm-public-key>
                <auth-server-url>http://localhost:9080/auth</auth-server-url>
                <ssl-required>external</ssl-required>
            </secure-deployment>
        </subsystem>
	...
	</profile>

8. To test this, you will needs a HTTP client like Boomerang Chrome Extension, Postman Chrome Extension, soapUI, or simply the curl command.
	
Running the quickstart
======================

Wildfly
----------

1. Start Wildfly in standalone mode :

        ${AS}/bin/standalone.sh

2. Build and deploy the demo :

        mvn install -Pdeploy -Pwildfly

3. Use Postman/Boomerang/curl to get a valid token from Keycloak
GET {{keycloak.url}}/auth/realms/demo2/protocol/openid-connect/token
HEADERS:
grant_type: password
client_id: postman
username: test@redhat.com
password: test@redhat.com

4. Check the keycloak server response and copy the "access_token" from it:
{
    "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0ZTM0NGFmOS1mMmFiLTQwY2MtOTBlNS01YWVmMGUzOTEyNGQiLCJleHAiOjE0NTI4OTk0OTksIm5iZiI6MCwiaWF0IjoxNDUyODk5NDM5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwODAvYXV0aC9yZWFsbXMvZGVtbzIiLCJhdWQiOiJwb3N0bWFuIiwic3ViIjoiZGJmNTQwOTItNGZlZS00YTRiLWE0MDMtYTAxOWVkNzRjNTBhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicG9zdG1hbiIsInNlc3Npb25fc3RhdGUiOiIzYTZmOTAxNS02MDU4LTRlZWQtODE3OC04NzI2N2I1MmU4YjciLCJjbGllbnRfc2Vzc2lvbiI6IjJiY2VlM2FkLTIwYWItNDA3OC05YTY4LTcyYzRlODk3ZTkyNCIsImFsbG93ZWQtb3JpZ2lucyI6W10sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicG9saWN5LXNlY3VyaXR5LW9wZW5pZCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Il19fSwibmFtZSI6IlRlc3QgTGFzdFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0QHJlZGhhdC5jb20iLCJnaXZlbl9uYW1lIjoiVGVzdCIsImZhbWlseV9uYW1lIjoiTGFzdFRlc3QiLCJlbWFpbCI6InRlc3RAcmVkaGF0LmNvbSJ9.lr8o3DJo9xpwmgdUJcbOPIWs5-bAnPt5fGr0h45xljBflq-hL3YZQy37znFmmuRDi6vRV8ReHleLRjKOklgjnS8KU65k6DP1JElpiqRYH1Oj_MAVcBJSKoz8Cu7X8PTtARwqVtSfTP_HZRleWVUzpz17ji2kAPvdWTBhY9FgDpVqkfUBgnyQlseKF1z3wvQS2McxIgFzUfmWC08UWmGuKFPq9BW4wgJWzzeDyrUaABcNFLZrevq7fgHL_TaLThtjuRxM-zOOgAGZRsYLeFY3AGM4aVNmhMksZ0LnXjib7vaBLNB9-LHPjjH_4VW5kK5TQf5oPi80CtV0vIv2jjayFQ",
    "expires_in": 60,
    "refresh_expires_in": 600,
    "refresh_token": "......",
    "token_type": "bearer",
    "id_token": "......",
    "not-before-policy": 0,
    "session-state": "....."
}

5. Use Postman/Boomerang/curl to call the quickstart REST service:
GET {{wildfly.url}}/policy-security-openid/rest/order/1
HEADERS:
Accept: text/xml
Authorization: Bearer <ACCESS_TOKEN>

For example:
GET localhost:8080/policy-security-openid/rest/order/5
HEADERS:
Accept: text/xml
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0ZTM0NGFmOS1mMmFiLTQwY2MtOTBlNS01YWVmMGUzOTEyNGQiLCJleHAiOjE0NTI4OTk0OTksIm5iZiI6MCwiaWF0IjoxNDUyODk5NDM5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwODAvYXV0aC9yZWFsbXMvZGVtbzIiLCJhdWQiOiJwb3N0bWFuIiwic3ViIjoiZGJmNTQwOTItNGZlZS00YTRiLWE0MDMtYTAxOWVkNzRjNTBhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicG9zdG1hbiIsInNlc3Npb25fc3RhdGUiOiIzYTZmOTAxNS02MDU4LTRlZWQtODE3OC04NzI2N2I1MmU4YjciLCJjbGllbnRfc2Vzc2lvbiI6IjJiY2VlM2FkLTIwYWItNDA3OC05YTY4LTcyYzRlODk3ZTkyNCIsImFsbG93ZWQtb3JpZ2lucyI6W10sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicG9saWN5LXNlY3VyaXR5LW9wZW5pZCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Il19fSwibmFtZSI6IlRlc3QgTGFzdFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0QHJlZGhhdC5jb20iLCJnaXZlbl9uYW1lIjoiVGVzdCIsImZhbWlseV9uYW1lIjoiTGFzdFRlc3QiLCJlbWFpbCI6InRlc3RAcmVkaGF0LmNvbSJ9.lr8o3DJo9xpwmgdUJcbOPIWs5-bAnPt5fGr0h45xljBflq-hL3YZQy37znFmmuRDi6vRV8ReHleLRjKOklgjnS8KU65k6DP1JElpiqRYH1Oj_MAVcBJSKoz8Cu7X8PTtARwqVtSfTP_HZRleWVUzpz17ji2kAPvdWTBhY9FgDpVqkfUBgnyQlseKF1z3wvQS2McxIgFzUfmWC08UWmGuKFPq9BW4wgJWzzeDyrUaABcNFLZrevq7fgHL_TaLThtjuRxM-zOOgAGZRsYLeFY3AGM4aVNmhMksZ0LnXjib7vaBLNB9-LHPjjH_4VW5kK5TQf5oPi80CtV0vIv2jjayFQ

6. Check the server response. You will get something like this:
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<order>
    <orderId>5</orderId>
    <orderItem>
        <item>
            <itemId>5000</itemId>
            <name>Item</name>
        </item>
        <quantity>1</quantity>
    </orderItem>
</order>

7. Try again if you wants with a wrong access token and check the error response.

8. Now try the SOAP service. Get another access_token again (steps 3 and 4, or a refresh-token).

9. Use Postman/Boomerang/curl to call the quickstart SOAP service:
POST {{wildfly.url}}/policy-security-openid/WorkService
BODY:
<x:Envelope xmlns:x="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:switchyard-quickstart-demo:policy-security-openid:0.1.0">
    <x:Header/>
    <x:Body>
        <urn:doWork>
            <urn:work>
                <urn:command>XXXX</urn:command>
            </urn:work>
        </urn:doWork>
    </x:Body>
</x:Envelope>
HEADERS:
Authorization: Bearer <ACCESS_TOKEN>
Content-Type: text/xml; charset=utf-8
SOAPAction: urn:switchyard-quickstart-demo:policy-security-openid:0.1.0

10. Check the server response. You will get something like this:
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"/>
    <soap:Body>
        <policy-security-openid:doWorkResponse xmlns:policy-security-openid="urn:switchyard-quickstart-demo:policy-security-openid:0.1.0">
            <workAck>
                <command>null</command>
                <received>true</received>
            </workAck>
        </policy-security-openid:doWorkResponse>
    </soap:Body>
</soap:Envelope>


11. Try again if you wants for example using a bad access_token.

12. Check the server response. You will get something like this:
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <soap:Fault>
            <faultcode>soap:Server</faultcode>
            <faultstring>SWITCHYARD014014: Required policies have not been provided: {http://docs.oasis-open.org/ns/opencsa/sca/200912}authorization {http://docs.oasis-open.org/ns/opencsa/sca/200912}clientAuthentication</faultstring>
        </soap:Fault>
    </soap:Body>
</soap:Envelope>

11. Undeploy the application

        mvn clean -Pdeploy -Pwildfly



Note: You can try again with these users...

admin (pass admin) -> with the realm role "admin" 
test@redhat.com (pass test@redhat.com) -> with the application role "policy-security-openid:user"
test_invalid@redhat.com (pass test_invalid@redhat.com) -> with the unauthorized realm role "user"

