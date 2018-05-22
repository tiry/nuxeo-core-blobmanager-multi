package org.nuxeo.ecm.core;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.blob.AbstractBlobProvider;
import org.nuxeo.ecm.core.blob.BlobInfo;
import org.nuxeo.ecm.core.blob.BlobManager;
import org.nuxeo.ecm.core.blob.BlobProvider;
import org.nuxeo.ecm.core.blob.binary.AbstractBinaryManager;
import org.nuxeo.ecm.core.blob.binary.BinaryBlobProvider;
import org.nuxeo.ecm.core.blob.binary.BinaryManager;
import org.nuxeo.runtime.api.Framework;

public class RWRouterBlobManager extends AbstractBlobProvider {

	BlobProvider masterProvider;

	BlobProvider stagingProvider;

	public static final String MASTER_PROVIDER_ID = "masterProviderId";
	public static final String STAGING_PROVIDER_ID = "stagingProviderId";

	private static final Log log = LogFactory.getLog(RWRouterBlobManager.class);

	@Override
	public void initialize(String blobProviderId, Map<String, String> properties) throws IOException {
		this.blobProviderId = blobProviderId;
		this.properties = properties;

		BlobManager bm = Framework.getService(BlobManager.class);

		masterProvider = bm.getBlobProvider(MASTER_PROVIDER_ID);
		stagingProvider = bm.getBlobProvider(STAGING_PROVIDER_ID);

		//disableGC(masterProvider);
	}
	
	protected void disableGC(BlobProvider provider) {
		if (provider!=null && provider instanceof BinaryBlobProvider) {
			BinaryManager bm=((BinaryBlobProvider)provider).getBinaryManager();			
			//((AbstractBinaryManager)bm).garbageCollector
		}
	}

	@Override
	public void close() {
		if (stagingProvider != null) {
			stagingProvider.close();
		}
		masterProvider.close();
	}

	@Override
	public Blob readBlob(BlobInfo blobInfo) throws IOException {

		if (stagingProvider != null) {
			try {
				return stagingProvider.readBlob(blobInfo);
			} catch (IOException e) {
				log.debug("unable to find binary in staging provider, falling back to production", e);
			}
		}
		return masterProvider.readBlob(blobInfo);
	}

	@Override
	public String writeBlob(Blob blobInfo) throws IOException {
		if (stagingProvider != null) {
			return stagingProvider.writeBlob(blobInfo);
		} else {
			// XXX add extra guard to protect from configuration mistakes?
			return masterProvider.writeBlob(blobInfo);
		}
	}

}
