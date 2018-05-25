package org.nuxeo.ecm.core;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.blob.BlobInfo;
import org.nuxeo.ecm.core.blob.BlobManager;
import org.nuxeo.ecm.core.blob.BlobProvider;
import org.nuxeo.ecm.core.blob.SimpleManagedBlob;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(CoreFeatureMultiBlobStore.class)
public class TestRepositoryMultiBlobStore {

	@Inject
	protected BlobManager blobManager;
	
    @Inject
    protected CoreSession coreSession;

	protected Blob mkBlob(String name) {
		return new StringBlob(UUID.randomUUID().toString(), "text/plain", "UTF-8", name);
	}

	@Test
	public void test() throws Exception {
		
		BlobProvider roProvider = blobManager.getBlobProvider("RO");
		assertNotNull(roProvider);
		String b1 = roProvider.writeBlob(mkBlob("prod01.txt"));

		BlobProvider rwProvider = blobManager.getBlobProvider("RW");
		assertNotNull(rwProvider);
		String b2 = rwProvider.writeBlob(mkBlob("preprod01.txt"));
		
		BlobInfo bi = new BlobInfo();
		
		DocumentModel doc = coreSession.createDocumentModel("/", "file1", "File");	
		bi.key=coreSession.getRepositoryName()+ ":" + b1;
		Blob blob = new SimpleManagedBlob(bi);		
		doc.setPropertyValue("file:content", (Serializable) blob);		
		doc=coreSession.createDocument(doc);
		
		doc = coreSession.createDocumentModel("/", "file2", "File");		
		bi.key=coreSession.getRepositoryName()+ ":" + b2;
		blob = new SimpleManagedBlob(bi);		
		doc.setPropertyValue("file:content", (Serializable) blob);		
		doc=coreSession.createDocument(doc);
				
		
	}
}
