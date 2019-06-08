package com.icat.epicenter.pmd;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BannedEqualsEqualsCheckTest extends SimpleAggregatorTst {
    @Override
    public void setUp() {
        addRule("category/icat/oldpmd.xml", "BannedEqualsEqualsCheck");
    }
}
