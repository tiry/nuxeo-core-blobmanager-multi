package org.nuxeo.ecm.core;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.ecm.core.test.CoreFeature;

/**
 * Empty Unit Testing class.
 * <p/>
 * 
 * @see <a href="https://doc.nuxeo.com/corg/unit-testing/">Unit Testing</a>
 */
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.ecm.core.nuxeo-core-binarymanager-multi-core")
public class TestMultiBinaryManager {

    @Inject
    protected CoreSession session;

    @Test
    public void emptyTest() {
        assertNull(null);
    }
}
