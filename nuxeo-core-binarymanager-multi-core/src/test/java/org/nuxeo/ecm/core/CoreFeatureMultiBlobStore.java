package org.nuxeo.ecm.core;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.StorageConfiguration;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.RuntimeFeature;
import org.nuxeo.runtime.test.runner.RuntimeHarness;
import org.osgi.framework.Bundle;;

@LocalDeploy("org.nuxeo.ecm.core.nuxeo-core-binarymanager-multi-core:test-blob-providers.xml")
public class CoreFeatureMultiBlobStore extends CoreFeature {

	protected class MultiStorageConfiguration extends StorageConfiguration {

		public MultiStorageConfiguration(CoreFeature feature) {
			super(feature);
		}

		@Override
		public URL getBlobManagerContrib(FeaturesRunner runner) {
			String bundleName = "org.nuxeo.ecm.core.nuxeo-core-binarymanager-multi-core";
			String contribPath = "muli-blobstore-contrib.xml";
			RuntimeHarness harness = runner.getFeature(RuntimeFeature.class).getHarness();
			Bundle bundle = harness.getOSGiAdapter().getRegistry().getBundle(bundleName);
			URL contribURL = bundle.getEntry(contribPath);
			assertNotNull("deployment contrib " + contribPath + " not found", contribURL);
			return contribURL;
		}
	}

	@Override
	public StorageConfiguration getStorageConfiguration() {
		return new MultiStorageConfiguration(this);
	}

}
