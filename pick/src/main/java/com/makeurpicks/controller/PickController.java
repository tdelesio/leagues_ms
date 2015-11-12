package com.makeurpicks.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.LeagueResponse;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.service.PickService;
import com.netflix.discovery.DiscoveryClient;

@RestController
@RequestMapping(value="/picks")
public class PickController  {

	@Autowired
	private PickService pickService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/weekid/{weekid}")
	public @ResponseBody Map<String, Map<String, Pick>> getPicksByWeek(@PathVariable String weekid)
	{
		return pickService.getPicksByWeek(weekid);
	}

	@RequestMapping(method=RequestMethod.GET, value="/self/weekid/{weekid}")
	public @ResponseBody Map<String, Pick> getPicksByWeekAndPlayer(Principal user, @PathVariable String weekid)
	{
		return pickService.getPicksByWeekAndPlayer(weekid, user.getName());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/player/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody Map<String, Pick> getPicksByWeekAndPlayer(Principal user, @PathVariable String weekid, @PathVariable String playerid)
	{
		return pickService.getOtherPicksByWeekAndPlayer(weekid, playerid);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Pick makePick(Principal user, @RequestBody Pick pick)
	{
//		pick.setPlayerId(user.getName());
		pick.setPlayerId("tim");
		return pickService.makePick(pick);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/")
	public @ResponseBody Pick updatePick(Principal user, @RequestBody Pick pick)
	{
		pick.setPlayerId(user.getName());
		return pickService.updatePick(pick, pick.getPlayerId());
	}
	
	
	@RequestMapping(method=RequestMethod.PUT, value="/double")
	public @ResponseBody DoublePick makeDoublePick(Principal user, @RequestBody DoublePick pick)
	{
		return pickService.makeDoublePick(pick.getPickId(), user.getName());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/double/weekid/{weekid}")
	public @ResponseBody DoublePick getDoublePick(Principal user, @PathVariable String weekid)
	{
		return pickService.getDoublePick(weekid, user.getName());
	}
	
//	@Autowired
//	private LeagueClient leagueClient;
	
	 @Autowired
	 private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancer;
	
//	@Autowired
//	RestTemplate restTemplate;
	
	@RequestMapping(method=RequestMethod.GET, value="/feign")
	public void getDoublePick(@CookieValue("SESSION")String cookie, Principal user)
	{
//		 InstanceInfo instance =
//				 discoveryClient.getNextServerFromEureka("league", false);
//				URI uri = UriComponentsBuilder.fromHttpUrl(instance.getHomePageUrl())
//						.path("leagues/").path("2115832110620821288-9040108557106096781").build().encode()
//						.toUri();
		System.out.println("Cookie= "+cookie);
		ServiceInstance instance = loadBalancer.choose("gateway");
		URI uri = UriComponentsBuilder.fromHttpUrl(String.format("http://%s:%s",
				instance.getHost(), instance.getPort()))
				.path("leagues/")
				.path("2115832110620821288-9040108557106096781")
				.build().encode()
				.toUri();
		System.out.println(uri);
		System.out.println("######################"+user.getName());
//		leagueClient.getLeagueById("22d65d17%2D09b9%2D4e4b%2Db7e1%2D53f8f6b060b4");
//		FeignLeagueClient lc = Feign.builder().requestInterceptor(new BasicAuthRequestInterceptor("user", "6d770f0f-330f-4ccb-8a15-3bebb2b5c87d"))
//		.target(FeignLeagueClient.class, uri.toString());
		
//		lc.getLeagueById("22d65d17-09b9-4e4b-b7e1-53f8f6b060b4");
//		lc.getLeagueById("-65d17%2D09b9%2D4e4b%2Db7e1%2D53f8f6b060b4");
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", "SESSION=" + cookie);
		HttpEntity<?> requestEntity = new HttpEntity(null, requestHeaders);
		
//		RestTemplate restTemplate = new RestTemplate();
//		LeagueResponse response = restTemplate.getForObject(uri, LeagueResponse.class);
//		ResponseEntity<LeagueResponse> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,LeagueResponse.class);
		
//		CookieRestTemplate restTemplate = new CookieRestTemplate();
		StatefullRestTemplate restTemplate = new StatefullRestTemplate();
		LeagueResponse response = restTemplate.getForObject(uri, LeagueResponse.class);
		
		System.out.println(response.getId());
	}
	
	public class CookieRestTemplate extends RestTemplate {

		private String session;
		private String xsrf_token;
		
		private static final String SESSION_COOKIE = "SESSION";
		private static final String XSRF_TOKEN_COOKIE = "XSRF-TOKEN";
		
		  @Override
		  protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
		    ClientHttpRequest request = super.createRequest(url, method);
System.out.println(buildCookie());
		    request.getHeaders().add("Cookie", buildCookie());
		    return request;
		  }
		  
		  private String buildCookie()
		  {
			  StringBuilder builder = new StringBuilder(SESSION_COOKIE);
			  builder.append("=");
			  builder.append(session);
			  builder.append(",");
			  builder.append(XSRF_TOKEN_COOKIE);
			  builder.append("=");
			  builder.append(xsrf_token);
			  return builder.toString();
		  }
	}
	
	public class StatefullRestTemplate extends RestTemplate
	{
	    private final HttpClient httpClient;
	    private final CookieStore cookieStore;
	    private final HttpContext httpContext;
	    private final StatefullHttpComponentsClientHttpRequestFactory statefullHttpComponentsClientHttpRequestFactory;

	    public StatefullRestTemplate()
	    {
	        super();
	        HttpParams params = new BasicHttpParams();
	        HttpClientParams.setRedirecting(params, false);

	        httpClient = new DefaultHttpClient(params);
	        cookieStore = new BasicCookieStore();
	        httpContext = new BasicHttpContext();
	        httpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
	        statefullHttpComponentsClientHttpRequestFactory = new StatefullHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
	        super.setRequestFactory(statefullHttpComponentsClientHttpRequestFactory);
	    }

	    public HttpClient getHttpClient()
	    {
	        return httpClient;
	    }

	    public CookieStore getCookieStore()
	    {
	        return cookieStore;
	    }

	    public HttpContext getHttpContext()
	    {
	        return httpContext;
	    }

	    public StatefullHttpComponentsClientHttpRequestFactory getStatefulHttpClientRequestFactory()
	    {
	        return statefullHttpComponentsClientHttpRequestFactory;
	    }

	}


	public class StatefullHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory
	{
	    private final HttpContext httpContext;

	    public StatefullHttpComponentsClientHttpRequestFactory(HttpClient httpClient, HttpContext httpContext)
	    {
	        super(httpClient);
	        this.httpContext = httpContext;
	    }

	    @Override
	    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
	    {
	        return this.httpContext;
	    }
	}
}