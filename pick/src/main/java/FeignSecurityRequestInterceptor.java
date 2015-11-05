import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignSecurityRequestInterceptor implements RequestInterceptor {

//	private final String header;
	
	@Override
	public void apply(RequestTemplate template) {
		System.out.println("*************************FeignSecurityRequestInterceptor");
	}

}
