package generators.network.dns.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Cache the entries returned by name servers.
 */
public class DNSCache {
	// cache size
	private int size;
	// current slot
	private int cachePtr = 0;
	// the actual cache
	private DNSCacheElement cache[];
	
	/**
	 * Create a new query cache for a name server.
	 * 
	 * @param mySize The size of the cache (also known as TTL)
	 */
	public DNSCache(int mySize) {
		size = mySize;
		
		// init cache
		cache = new DNSCacheElement[size];
		for(int i = 0; i < cache.length; i++) {
			cache[i] = new DNSCacheElement();
		}
		
		cachePtr = 0;

	}

	/**
	 * Add a new query result to the cache
	 * 
	 * @param query The query sent by the local system
	 * @param res The result returned by the remote system on the query
	 */
	public void add(String query, DNSResult res) {
		cache[cachePtr].put(query, res);
	}
	
	/**
	 * Check if there is a result cached for a specific query.
	 * 
	 * @param query The query to look for
	 * @return The result or null if none exists
	 */
	public DNSResult get(String query) {
		DNSResult res = null;
		
		for(int i = 0; i < cache.length; i++) {
			// only query cache if the current element is not the one written to
			if(i != cachePtr && cache[i].get(query) != null) {
				res = cache[i].get(query);
				cache[cachePtr].put(query, res);
			}
		}
		
		return res;
	}
	
	/**
	 * Advance the cache to the next slot.
	 * Rolls round if last slot was reached.
	 */
	public void nextSlot() {
		if(cachePtr < size - 1) {
			cachePtr++;
		} else {
			cachePtr = 0;
		}
		cache[cachePtr].clear();
	}
	
	/**
	 * Get the cache's size.
	 * 
	 * @return The size of the cache
	 */
	public int getSize() {
		return cache.length;
	}	
}
