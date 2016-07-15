/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.jotform;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

public class JotformConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap;

    private Map<String, String> apiRequestHeadersMap;

    private String apiEndpointUrl;

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        init("jotform-connector-1.0.2-SNAPSHOT");

        esbRequestHeadersMap = new HashMap<String, String>();
        apiRequestHeadersMap = new HashMap<String, String>();

        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/json");

        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
        apiRequestHeadersMap.put("Content-Type", "application/x-www-form-urlencoded");
        apiRequestHeadersMap.put("APIKEY", connectorProperties.getProperty("apiKey"));
        apiEndpointUrl = connectorProperties.getProperty("apiUrl");

    }

    /**
     * Positive test case for cloneForm method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {cloneForm} integration test with mandatory parameters.")
    public void testCloneFormWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:cloneForm");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_cloneForm_mandatory.json");
        String clonedFormId = esbRestResponse.getBody().getJSONObject("content").getString("id");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Method Name: cloneForm
     * Skipped Case: optional case
     * Reason: No optional parameter(s) to assert.
     */

    /**
     * Negative test case for cloneForm method.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {cloneForm} integration test with negative case.")
    public void testCloneFormWithNegativeCase() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:cloneForm");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_cloneForm_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 404);
    }

    /**
     * Positive test case for getForm method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getForm} integration test with mandatory parameters.")
    public void testGetFormWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getForm");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getForm_mandatory.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Method Name: getForm
     * Skipped Case: optional case
     * Reason: No optional parameter(s) to assert.
     */

    /**
     * Negative test case for getForm method.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getForm} integration test with negative case.")
    public void testGetFormWithNegativeCase() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getForm");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getForm_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 404);
    }

    /**
     * Positive test case for getSubmission method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getSubmission} integration test with mandatory parameters.")
    public void testGetSubmissionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getSubmission");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubmission_mandatory.json");
        connectorProperties.setProperty("submissionNew",
                esbRestResponse.getBody().getJSONObject("content").getString("new"));
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Positive test case for getSubmission method with for update submission with optional parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getSubmission} integration test with mandatory parameters.")
    public void testGetSubmissionForUpdateSubmission() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getSubmission");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubmission_mandatory.json");
        JSONArray answerIds = esbRestResponse.getBody().getJSONObject("content").getJSONObject("answers").names();

        // Sorting to retrieve the answerIds in ascending order.
        Integer[] questionIds = new Integer[answerIds.length()];
        for (int i = 0; i < answerIds.length(); i++) {
            questionIds[i] = Integer.parseInt(answerIds.get(i).toString());
        }
        Arrays.sort(questionIds);

        String firstQuestionId = Integer.toString(questionIds[0]);
        String secondQuestionId = Integer.toString(questionIds[1]);
        JSONObject firstAnswer = esbRestResponse.getBody().getJSONObject("content").getJSONObject("answers").getJSONObject(firstQuestionId);
        JSONObject secondAnswer = esbRestResponse.getBody().getJSONObject("content").getJSONObject("answers").getJSONObject(secondQuestionId);

        String firstName = firstAnswer.getJSONObject("answer").getString("first");
        String lastName = firstAnswer.getJSONObject("answer").getString("last");

        connectorProperties.setProperty("firstName", firstName);
        connectorProperties.setProperty("lastName", lastName);
        connectorProperties.setProperty("firstQuestionId", firstQuestionId);

        connectorProperties.setProperty("secondQuestionId", secondQuestionId);
        String email = secondAnswer.getString("answer");
        connectorProperties.setProperty("email", email);

        connectorProperties.setProperty("submissionNew",
                esbRestResponse.getBody().getJSONObject("content").getString("new"));
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Method Name: getSubmission
     * Skipped Case: optional case
     * Reason: No optional parameter(s) to assert.
     */

    /**
     * Negative test case for getSubmission method.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getSubmission} integration test with negative case.")
    public void testGetSubmissionWithNegativeCase() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getSubmission");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubmission_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 404);

    }

    /**
     * Positive test case for getUserSubmissions method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getUserSubmissions} integration test with mandatory parameters.")
    public void testGetUserSubmissionsWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getUserSubmissions");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getUserSubmissions_mandatory.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Positive test case for getUserSubmissions method with optional parameters.
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getUserSubmissions} integration test with optional parameters.")
    public void testGetUserSubmissionsWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getUserSubmissions");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getUserSubmissions_optional.json");

        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("resultSet").getString("limit"),
                connectorProperties.getProperty("limit"));
    }

    /**
     * Method Name: getUserSubmissions
     * Skipped Case: negative case
     * Reason: No parameter(s) to test negative case.
     */

    /**
     * Positive test case for getFormSubmissions method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getFormSubmissions} integration test with mandatory parameters.")
    public void testGetFormSubmissionsWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getFormSubmissions");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getFormSubmissions_mandatory.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
    }

    /**
     * Positive test case for getFormSubmissions method with optional parameters.
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getFormSubmissions} integration test with optional parameters.")
    public void testGetFormSubmissionsWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getFormSubmissions");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getFormSubmissions_optional.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("resultSet").getString("limit"),
                connectorProperties.getProperty("limit"));
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("resultSet").getString("orderby"),
                connectorProperties.getProperty("orderby"));
    }

    /**
     * Negative test case for getFormSubmissions method.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {getFormSubmissions} integration test with negative case.")
    public void testGetFormSubmissionsWithNegativeCase() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getFormSubmissions");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getFormSubmissions_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 404);
    }

    /**
     * Positive test case for updateSubmission method with mandatory parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {updateSubmission} integration test with mandatory parameters.")
    public void testUpdateSubmissionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:updateSubmission");

        // Updating the submission.
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateSubmission_mandatory.json");
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
    }

    /**
     * Positive test case for updateSubmission method with optional parameters.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetSubmissionForUpdateSubmission"},
            description = "jotform {updateSubmission} integration test with optional parameters.")
    public void testUpdateSubmissionWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:updateSubmission");

        // Updating the submission.
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateSubmission_optional.json");
        Assert.assertEquals(esbRestResponse.getBody().getString("message"), "success");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 200);
    }

    /**
     * Negative test case for updateSubmission method. Trying to update providing only the submissionId.
     *
     * @throws JSONException
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "jotform {updateSubmission} integration test with negative case.")
    public void testUpdateSubmissionWithNegativeCase() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:updateSubmission");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateSubmission_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getInt("responseCode"), 401);
    }
}