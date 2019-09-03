package generators.network.dns;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractNetworkGenerator;
import generators.network.dns.anim.ARecordResultAnim;
import generators.network.dns.anim.DNSQueryAnim;
import generators.network.dns.anim.DNSResultAnim;
import generators.network.dns.anim.NSRecordResultAnim;
import generators.network.dns.helper.DNSCache;
import generators.network.dns.helper.DNSResult;
import generators.network.dns.helper.DNSResultAddress;
import generators.network.dns.helper.LocalNameServer;
import generators.network.dns.helper.NameServer;
import generators.network.dns.helper.RootNameServer;
import generators.network.helper.ClassName;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Generate an animation showing the basic functionality of the domain name system. 
 */
public class DNSQueryGenerator extends AbstractNetworkGenerator {		
	// default values
	private static final String[] DEFAULT_QUERY = {"www.host1.com", "www.host2.de", "www.host1.com", "www.sub.host2.de", 
			"www.host3.de", "www.host1.de", "sub.host3.de"};
	private static final int DEFAULT_TTL = 3;
	
	// dns cache
	private int ttl;
	private DNSCache cache;
	
	// display info
	InfoBox ib;
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public DNSQueryGenerator() {
		this(Locale.GERMANY);
	}
	
	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public DNSQueryGenerator(Locale myLocale) {
		textResource = ClassName.getPackageAsPath(this) + "resources/" + ClassName.getClassNameOnly(this);
		locale = myLocale;
		translator = new Translator(textResource, locale);
	}

	/**
	 * Create a new generator using the given language object.
	 * This constructor is used internally to setup the animation after 
	 * the initial preparation of any primitives.
	 * 
	 * @param lang The Language object to use within the animation
	 * @param myLocale The locale setting to use
	 */
	private DNSQueryGenerator(Language lang, Locale myLocale) {
		this(myLocale);
		
		s = new NetworkStyle();
		
		l = lang;
		l.setStepMode(true);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// get Queries from primitives
		Object queryPrim = primitives.get("Domain List");
		String[] query;
		if(queryPrim instanceof String[]) {
			query = (String[]) queryPrim;
		} else {
			query = DEFAULT_QUERY;
		}
		
		// get TTL from primitives
		Object ttlPrim = primitives.get("Time To Live");
		ttl = DEFAULT_TTL;
		if (ttlPrim instanceof Integer) {
			ttl = (Integer) ttlPrim;
		}
		
		// Create a new animation
		init();
		DNSQueryGenerator anim = new DNSQueryGenerator(l, locale);
		
		// build headline 
		anim.getHeader();
		
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));
		
		// build title
		anim.getTitleSlide(ttl);
		
		// use info box
		anim.createInfoBox();
		
		// build the animation
		anim.createClient();
		anim.createCachingNameServer();
		
		
		// create a list of domain names and associated name servers 
		// use a map to guarantee uniqueness
		Set<String> nameServers = getNameServers(query);
		
		// distribute the needed nameserver evenly onto the screen
		int border = 30;
		int spacing = (ANIM_HEIGHT - border * 2) / (nameServers.size() + 1);
		int count = 1;
		
		anim.createIterativeNameServer(".", border);
		
		for(String thisNS : nameServers) {
			anim.createIterativeNameServer(thisNS, border + (spacing * count));
			count++;
		}
		
		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(query, ttl);

		return l.toString();
	}
	
	/**
	 * Run the actual algorithm querying domain name servers 
	 * and returning results to the client.
	 * 
	 * @param query The queries to execute
	 * @param timeToLive The cache size to use
	 */
	private void run(String[] query, int timeToLive) {
		// init cache
		cache = new DNSCache(timeToLive + 1);
		
		// run each query
		for (String thisQuery : query) {
			thisQuery = thisQuery.trim().toLowerCase();
			
			// if we got an empty query just jump to the next one
			if(thisQuery.isEmpty()) {
				break;
			}
			
			boolean resolved = false;
			DNSResult nsRes = null;
			String ip = new String();

			// show query from client to local resolver...
			DNSQueryAnim lq = new DNSQueryAnim(l, "clientComputer", "localNameServer", thisQuery);
			
			// ...and display info box content
			int queryCount = 1;
			ArrayList<String> ibText = new ArrayList<String>();
			ibText.add(translator.translateMessage("HOSTNAME") + ": " + thisQuery);
			ibText.add(translator.translateMessage("QUERYCOUNT") + ": " + String.valueOf(queryCount));
			ib.setText(ibText);
			
			l.nextStep(translator.translateMessage("LBL_QUERY_NS", thisQuery));
			lq.hide();

			// always start at the local resolver
			NameServer lastNS = new LocalNameServer();

			// select next slot to write results form current query
			cache.nextSlot();
			
			// query the local cache to find an entry point
			Set<String> parts = getDomainParts(thisQuery);
			for(String thisPart : parts) {
				if(cache.get(thisPart) != null) {
					nsRes = cache.get(thisPart);
				}
			}
			
			// if we have a cache miss the entry point is the root name server
			if(nsRes == null) {
				nsRes = new RootNameServer();
			}
			
			// resolve hostname
			while(!resolved) {
				
				if(nsRes instanceof DNSResultAddress) {
					ip = ((DNSResultAddress)nsRes).getIP();
					
					// only display result if remote dns was queried
					// otherwise the result is shown in the last step
					if(!(lastNS instanceof LocalNameServer)) {
						DNSResultAnim animRes = new ARecordResultAnim(l, lastNS.getDomain(), "localNameServer", ip);
						l.nextStep();
						animRes.hide();
					}

					// add result to cache
					cache.add(thisQuery, nsRes);

					// if we got an IP address the hostname was resolved
					resolved = true;
				} else if(nsRes instanceof NameServer) {
					NameServer nextNS = (NameServer)nsRes;
					
					// add domain to cache
					cache.add(nextNS.getDomain(), nsRes);

					// only display result if remote dns was queried
					// otherwise the result is shown in the last step
					if(!(lastNS instanceof LocalNameServer)) {
						DNSResultAnim animRes = new NSRecordResultAnim(l, lastNS.getDomain(), "localNameServer", nextNS.getDomain());
						l.nextStep();
						animRes.hide();
					}

					// if the server does not know the ip the name was not resolved
					resolved = false;
										
					// query the nameserver
					nsRes = nextNS.query(thisQuery);
					
					// update query count and info box
					queryCount++;
					ibText.set(1, translator.translateMessage("QUERYCOUNT") + ": " + String.valueOf(queryCount));				
					ib.setText(ibText);

					// display query only in the current step
					DNSQueryAnim animQuery = new DNSQueryAnim(l, "localNameServer", nextNS.getDomain(), thisQuery);
					l.nextStep();
					animQuery.hide();
					
					lastNS = nextNS;
				} else {
					System.err.println("DNSQueryGenerator: The DNS Lookup made Boo Boo.");
					// if we got an error just stop the query
					resolved = true;
				}
			}
	
			// display result to client
			DNSResultAnim animRes = new ARecordResultAnim(l, "localNameServer", "clientComputer", ip);
			l.nextStep();
			animRes.hide();
		}
		l.nextStep();
	}

	/**
	 * Split up a host or domain name into its individual parts 
	 * such as hostname, subdomain, domain, top level domain.
	 * 
	 * @param dom The domain to split
	 * @return A Set of domain parts
	 */
	private Set<String> getDomainParts(String dom) {
		Set<String> parts = new LinkedHashSet<String>();

		String[] domArr = dom.split("\\.");
		StringBuilder nsb = new StringBuilder();
		// no need to add the hostname so we start at 1
		for(int i = domArr.length - 1; i >= 0; i--) {
			nsb.insert(0, domArr[i]);
			parts.add(nsb.toString());
			nsb.insert(0, ".");
		}
	
		return parts;
	}
	
	/**
	 * Get all domain names, servers are needed for the animation.
	 * 
	 * @param query The queries used in the animation
	 * @return A Set of domain names servers are needed for
	 */
	private Set<String> getNameServers(String[] query) {
		Set<String> servers = new LinkedHashSet<String>();
		String[] queryArr;
		for(String thisQuery : query) {	
			queryArr = thisQuery.split("\\.");
			StringBuilder nsb = new StringBuilder();
			// no need to add the hostname so we start at 1
			for(int i = queryArr.length - 1; i > 0; i--) {
				nsb.insert(0, queryArr[i]);
				servers.add(nsb.toString());
				nsb.insert(0, ".");
			}
		}
		return servers;
	}
	
	/**
	 * Create new instance of an info box.
	 */
	private void createInfoBox() {
		ib = new InfoBox(l, new Offset(75, 0, "header1", AnimalScript.DIRECTION_E), 2, translator.translateMessage("CURRENTQUERY"));
	}

	/**
	 * Create a client computer to send queries from.
	 * This only creates the view not the actual client computer object to work with.
	 */
	private void createClient() {
		int xPos = 150;
		int yPos = ANIM_HEIGHT / 2;
		int radius = 20;
		
		// define properties
		CircleProperties iconProps = new CircleProperties();
		iconProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		iconProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		iconProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		TextProperties labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		labelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);


		// create actual element
		l.newCircle(new Coordinates(xPos, yPos), radius, "clientComputer", null, iconProps);
		l.newText(new Offset(0, -(radius + 5), "clientComputer", AnimalScript.DIRECTION_N), "Client Computer", "lblClientComputer", null, labelProps);
	}
	
	/**
	 * Create a caching (local) name server.
	 * This only creates the view not the actual name server object to work with.
	 */
	private void createCachingNameServer() {
		int xPos = 450;
		int yPos = ANIM_HEIGHT / 2;
		int radius = 20;
		
		// define properties
		CircleProperties iconProps = new CircleProperties();
		iconProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.YELLOW);
		iconProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		iconProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		TextProperties labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		//labelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

		// create actual element
		l.newCircle(new Coordinates(xPos, yPos), radius, "localNameServer", null, iconProps);
		l.newText(new Offset(-(radius + 5), -(radius + 5), "localNameServer", AnimalScript.DIRECTION_NW), "Nameserver", "lblLocalNameServer1", null, labelProps);
		l.newText(new Offset(0, -13, "lblLocalNameServer1", AnimalScript.DIRECTION_NW), "Local", "lblLocalNameServer2", null, labelProps);
	}

	/**
	 * Create a iterative name server responsible for a domain.
	 * This only creates the view not the actual name server object to work with.
	 * 
	 * @param domain The domain the server is responsible for
	 * @param yPos The position within the animation
	 */
	private void createIterativeNameServer(String domain, int yPos) {
		int xPos = 750;
		int radius = 20;
		
		// define properties
		CircleProperties iconProps = new CircleProperties();
		iconProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		iconProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		iconProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		TextProperties labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		// create actual element
		l.newCircle(new Coordinates(xPos, yPos), radius, domain, null, iconProps);
		l.newText(new Offset(5, 0, domain, AnimalScript.DIRECTION_E), domain, "lbl" + domain, null, labelProps);
	}
}
