/*
 * (C) Copyright 2018 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Tiry
 */
package org.nuxeo.ecm.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.blob.BlobInfo;
import org.nuxeo.ecm.core.blob.BlobManager;
import org.nuxeo.ecm.core.blob.BlobManagerFeature;
import org.nuxeo.ecm.core.blob.BlobProvider;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

@RunWith(FeaturesRunner.class)
@Features(BlobManagerFeature.class)
@LocalDeploy("org.nuxeo.ecm.core.nuxeo-core-binarymanager-multi-core:test-blob-providers.xml")
public class TestMultiBinaryManager {

	@Inject
	protected BlobManager blobManager;

	protected Blob mkBlob(String name) {
		return new StringBlob(UUID.randomUUID().toString(), "text/plain", "UTF-8", name);
	}

	@Test
	public void testMultiProviders() throws Exception {

		BlobProvider roProvider = blobManager.getBlobProvider("RO");
		assertNotNull(roProvider);
		String b1 = roProvider.writeBlob(mkBlob("prod01.txt"));
		String b2 = roProvider.writeBlob(mkBlob("prod02.txt"));

		BlobProvider rwProvider = blobManager.getBlobProvider("RW");
		assertNotNull(rwProvider);
		String b3 = rwProvider.writeBlob(mkBlob("preprod01.txt"));
		String b4 = rwProvider.writeBlob(mkBlob("preprod02.txt"));

		BlobProvider multiProvider = blobManager.getBlobProvider("Multi");
		assertNotNull(multiProvider);
		String b5 = multiProvider.writeBlob(mkBlob("preprod03.txt"));
		String b6 = multiProvider.writeBlob(mkBlob("preprod04.txt"));

		BlobInfo bi = new BlobInfo();

		bi.key=b1;
		assertNotNull(multiProvider.readBlob(bi));
		bi.key=b2;
		assertNotNull(multiProvider.readBlob(bi));
		
		bi.key=b3;
		assertNotNull(multiProvider.readBlob(bi));
		bi.key=b4;
		assertNotNull(multiProvider.readBlob(bi));
		
		bi.key=b5;
		assertNotNull(multiProvider.readBlob(bi));
		assertNotNull(rwProvider.readBlob(bi));
		try {
			assertNull(roProvider.readBlob(bi));
			fail("Blob should not be avalaible in the RO provider");
		} catch (Exception e) {}
		
		bi.key=b6;
		assertNotNull(multiProvider.readBlob(bi));
		assertNotNull(rwProvider.readBlob(bi));		
		try {
			assertNull(roProvider.readBlob(bi));
			fail("Blob should not be avalaible in the RO provider");
		} catch (Exception e) {}	}

}
