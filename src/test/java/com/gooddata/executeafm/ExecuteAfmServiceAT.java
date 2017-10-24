/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm;

import com.gooddata.AbstractGoodDataAT;
import com.gooddata.executeafm.afm.AttributeItem;
import com.gooddata.executeafm.afm.ObjectAfm;
import org.testng.annotations.Test;

public class ExecuteAfmServiceAT extends AbstractGoodDataAT {

    @Test(groups = "executeafm", dependsOnGroups = "dataset", enabled = false)
    public void testExecute() throws Exception {
        final Execution execution = new Execution(new ObjectAfm()
                .addAttribute(new AttributeItem(new ObjIdentifierQualifier("attr.person.name"), "localId"))
                .addAttribute(new AttributeItem(new ObjIdentifierQualifier("attr.person.role"), "localId")));
        gd.getExecuteAfmService().executeAfm(project, execution).get();
    }
}