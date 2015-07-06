package org.bricks.utils;

import org.bricks.utils.Cache.DataProvider;

public class ThreadTransferCache<T extends ThreadTransferCache.TransferData> extends Cache.LocalCache<T> {
	
	private static final int cacheSize = 64;
	private Quarantine<T> portal = new Quarantine<T>(cacheSize);

	public ThreadTransferCache(DataProvider<T> dataProvider) {
		super(dataProvider);
	}

	@Override
	protected T getLocal() {
		T data = portal.poll();
		if(data == null){
			data = provideNew();
			data.setPortal(portal);
		}
		return data;
	}

	@Override
	protected void putLocal(T data) {
		data.getPortal().push(data);
	}
	
	public interface TransferData{
		public void setPortal(Quarantine<? extends ThreadTransferCache.TransferData> portal);
		public Quarantine<ThreadTransferCache.TransferData> getPortal();
	}

	public static class AbstractTransferData implements TransferData{
		
		private Quarantine<? extends TransferData> portal;

		public void setPortal(Quarantine<? extends TransferData> portal) {
			this.portal = portal;
		}

		public Quarantine<TransferData> getPortal() {
			return (Quarantine<TransferData>) portal;
		}
	}
}
