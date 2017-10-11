/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.response

import com.gooddata.executeafm.response.AttributeHeader
import com.gooddata.executeafm.response.ExecutionResponse
import com.gooddata.executeafm.response.MeasureGroupHeader
import com.gooddata.executeafm.response.MeasureHeaderItem
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class ExecutionResponseTest extends Specification {

    // todo serialize

    def "should deserialize"() {
        when:
        ExecutionResponse result = readObjectFromResource('/executeafm/response/executionResponse.json', ExecutionResponse)

        then:
        result.links["uri"] == "poll"

        result.dimensions.size() == 1
        result.dimensions[0].name == "x"
        result.dimensions[0].headers.size() == 3

        AttributeHeader account = result.dimensions[0].headers[0] as AttributeHeader
        account.name == "Account"
        account.uri == "/gdc/md/FoodMartDemo/obj/124"

        AttributeHeader accountType = result.dimensions[0].headers[1] as AttributeHeader
        accountType.name == "Account Type"
        accountType.uri == "/gdc/md/FoodMartDemo/obj/113"

        MeasureGroupHeader measureGroupHeader = result.dimensions[0].headers[2] as MeasureGroupHeader

        List<MeasureHeaderItem> metricHeaderItems = measureGroupHeader.items as List<MeasureHeaderItem>
        metricHeaderItems[0].uri == "0" // todo
        metricHeaderItems[0].name == "Accounting Amount [Sum]"
        metricHeaderItems[1].uri == "0" // todo
        metricHeaderItems[1].name == "Accounting Amount [Avg]"

    }

}
