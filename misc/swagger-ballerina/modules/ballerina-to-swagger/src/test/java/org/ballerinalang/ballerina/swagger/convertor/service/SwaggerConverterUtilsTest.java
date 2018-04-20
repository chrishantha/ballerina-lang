/*
 * Copyright (c) 2018, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballerinalang.ballerina.swagger.convertor.service;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Ballerina conversion to swagger and open API will test in this class.
 */

public class SwaggerConverterUtilsTest {
    //Sample Ballerina Service definitions to be used for tests.
    private static String echoServiceBal = "import ballerina/http;\n" +
            "\n" +
            "endpoint http:Listener listener {\n" +
            "    port:9090\n" +
            "};\n" +
            "\n" +
            "service<http:Service> hello bind listener {\n" +
            "    hi (endpoint caller, http:Request request) {\n" +
            "        http:Response res;\n" +
            "        res.setStringPayload(\"Hello World!\\n\");\n" +
            "        _ = caller->respond(res);\n" +
            "    }\n" +
            "}";

    private static String serviceWithMultipleHTTPMethodsInResourceLevel = "import ballerina/http;\n" +
            "\n" +
            "\n" +
            "endpoint http:Listener backendEP {\n" +
            "   port:8081\n" +
            "};\n" +
            "\n" +
            "endpoint http:Client backendClientEP {\n" +
            "   targets: [{url: \"http://localhost:8081\"}]\n" +
            "};\n" +
            "\n" +
            "\n" +
            "@http:ServiceConfig {\n" +
            "   basePath:\"/hello\"\n" +
            "   \n" +
            "}\n" +
            "service hello bind backendEP {\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"GET, PUT\"],\n" +
            "       path:\"/\"\n" +
            "   }\n" +
            "   sayHello (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "}\n";

    private static String serviceWithNoHTTPMethodsAtResourceLevel = "import ballerina/http;\n" +
            "\n" +
            "\n" +
            "endpoint http:Listener backendEP {\n" +
            "   port:8081\n" +
            "};\n" +
            "\n" +
            "endpoint http:Client backendClientEP {\n" +
            "   targets: [{url: \"http://localhost:8081\"}]\n" +
            "};\n" +
            "\n" +
            "\n" +
            "@http:ServiceConfig {\n" +
            "   basePath:\"/hello\"\n" +
            "   \n" +
            "}\n" +
            "service hello bind backendEP {\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "       path:\"/\"\n" +
            "   }\n" +
            "   sayHello (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "}\n";

    private static String serviceWithOneHTTPMethod = "import ballerina/http;\n" +
            "\n" +
            "\n" +
            "endpoint http:Listener backendEP {\n" +
            "   port:8081\n" +
            "};\n" +
            "\n" +
            "endpoint http:Client backendClientEP {\n" +
            "   targets: [{url: \"http://localhost:8081\"}]\n" +
            "};\n" +
            "\n" +
            "\n" +
            "@http:ServiceConfig {\n" +
            "   basePath:\"/hello\"\n" +
            "   \n" +
            "}\n" +
            "service hello bind backendEP {\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"GET\"],\n" +
            "       path:\"/\"\n" +
            "   }\n" +
            "   sayHello (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "}\n";

    String sampleSwaggerWithTwoResources = "import ballerina/http;\n" +
            "\n" +
            "\n" +
            "endpoint http:Listener backendEP {\n" +
            "   port:8081\n" +
            "};\n" +
            "\n" +
            "endpoint http:Client backendClientEP {\n" +
            "   targets: [{url: \"http://localhost:8081\"}]\n" +
            "};\n" +
            "\n" +
            "\n" +
            "@http:ServiceConfig {\n" +
            "   basePath:\"/hello\"\n" +
            "   \n" +
            "}\n" +
            "service hello bind backendEP {\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"GET\"],\n" +
            "       path:\"/goma\"\n" +
            "   }\n" +
            "   sayHello (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"PUT\"],\n" +
            "       path:\"/ela\"\n" +
            "   }\n" +
            "   sayHelloPut (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "}";
    String sampleSwaggerWithMultipleResourceAndVerbs = "import ballerina/http;\n" +
            "\n" +
            "\n" +
            "endpoint http:Listener backendEP {\n" +
            "   port:8081\n" +
            "};\n" +
            "\n" +
            "endpoint http:Client backendClientEP {\n" +
            "   targets: [{url: \"http://localhost:8081\"}]\n" +
            "};\n" +
            "\n" +
            "\n" +
            "@http:ServiceConfig {\n" +
            "   basePath:\"/hello\"\n" +
            "   \n" +
            "}\n" +
            "service hello bind backendEP {\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"GET\"],\n" +
            "       path:\"/goma\"\n" +
            "   }\n" +
            "   sayHello (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "\n" +
            "   @http:ResourceConfig {\n" +
            "   methods:[\"PUT\"],\n" +
            "       path:\"/ela\"\n" +
            "   }\n" +
            "   sayHelloPut (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "\n" +
            "@http:ResourceConfig {\n" +
            "   methods:[\"POST\"],\n" +
            "       path:\"/ela\"\n" +
            "   }\n" +
            "   sayHelloPost (endpoint outboundEP, http:Request request) {\n" +
            "       http:Response response = new;\n" +
            "       response.setStringPayload(\"Hello World!!!\");\n" +
            "       _ = outboundEP -> respond(response);\n" +
            "   }\n" +
            "}";

    @Test(description = "Test ballerina to swagger conversion")
    public void testBallerinaToSwaggerConversion() {
        String serviceName = "";
        try {
            String swaggerDefinition = SwaggerConverterUtils.generateSwaggerDefinitions(
                    serviceWithMultipleHTTPMethodsInResourceLevel, serviceName);
            Assert.assertNotNull(swaggerDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition");
        }
    }


    @Test(description = "Test OAS definition generation from ballerina service which contains multiple HTTP methods" +
            "defined in same resource definition inline")
    public void testOASGenerationWithMultipleHTTPMethodsInResourceLevel() {
        String serviceName = "";
        try {
            String swaggerDefinition = SwaggerConverterUtils.generateOAS3Definitions(
                    serviceWithMultipleHTTPMethodsInResourceLevel, serviceName);
            Assert.assertNotNull(swaggerDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition with empty service name");
        }
    }


    @Test(description = "Test OAS definition generation from ballerina service which do not have any HTTP methods" +
            "defined at resource level")
    public void testOASGenerationWithNoHTTPMethodsAtResourceLevel() {
        String serviceName = "hello";
        try {
            String swaggerDefinition = SwaggerConverterUtils.generateOAS3Definitions(
                    serviceWithNoHTTPMethodsAtResourceLevel, serviceName);
            Assert.assertNotNull(swaggerDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition with empty service name");
        }
    }

    @Test(description = "Test OAS definition generation from ballerina service with resource which contains only" +
            "one http method defined")
    public void testOASGenerationWithSingleHTTPVerbResource() {
        String serviceName = "hello";
        try {
            String swaggerDefinition = SwaggerConverterUtils.generateOAS3Definitions(serviceWithOneHTTPMethod,
                    serviceName);
            Assert.assertNotNull(swaggerDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition with empty service name");
        }
    }

    @Test(description = "Test OAS definition generation from ballerina service which contains 2 resource which " +
            "contains only one http method in resource level")
    public void testOASGenerationWithTwoResource() {
        String serviceName = "hello";
        try {
            String swaggerDefinition = SwaggerConverterUtils.generateOAS3Definitions(
                    sampleSwaggerWithTwoResources, serviceName);
            Assert.assertNotNull(swaggerDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition with empty service name");
        }
    }

    @Test(description = "Test OAS and Swagger definition generation from ballerina service with multiple resource" +
            " which contains more than http method defined")
    public void testWithTwoResourceWithMultipleVerbs() {
        String serviceName = "hello";
        try {
            String openAPIDefinition = SwaggerConverterUtils.generateOAS3Definitions(
                    sampleSwaggerWithMultipleResourceAndVerbs, serviceName);
            String swaggerDefinition = SwaggerConverterUtils.generateSwaggerDefinitions(
                    sampleSwaggerWithMultipleResourceAndVerbs, serviceName);
            Assert.assertNotNull(swaggerDefinition);
            Assert.assertNotNull(openAPIDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina service to swagger definition with empty service name");
        }
    }

    @Test(description = "Test OAS and Swagger definition generation from ballerina echo service")
    public void testWithEchoService() {
        try {
            String openAPIDefinition = SwaggerConverterUtils.generateOAS3Definitions(
                    echoServiceBal, null);
            String swaggerDefinition = SwaggerConverterUtils.generateSwaggerDefinitions(
                    echoServiceBal, null);
            Assert.assertNotNull(swaggerDefinition);
            Assert.assertNotNull(openAPIDefinition);
        } catch (IOException e) {
            Assert.fail("Error while converting ballerina echo service to swagger definition");
        }
    }
}
