package uk.co.village_greens_coop.VillageGreensMemberPortal.config;import javax.servlet.Filter;import javax.servlet.MultipartConfigElement;import javax.servlet.ServletContext;import javax.servlet.ServletRegistration;import org.springframework.context.annotation.Bean;import org.springframework.core.annotation.Order;import org.springframework.web.context.request.RequestContextListener;import org.springframework.web.filter.CharacterEncodingFilter;import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;@Order(2)public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {	private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB	    @Override    protected String[] getServletMappings() {        return new String[]{"/"};    }    @Override    protected Class<?>[] getRootConfigClasses() {        return new Class<?>[] {ApplicationConfig.class, DataSourceConfig.class, JpaConfig.class, SecurityConfig.class, JavaMailConfig.class, TaskConfig.class, AspectConfig.class};    }    @Override    protected Class<?>[] getServletConfigClasses() {        return new Class<?>[] {WebMvcConfig.class};    }    @Override    protected Filter[] getServletFilters() {        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();        characterEncodingFilter.setEncoding("UTF-8");        characterEncodingFilter.setForceEncoding(true);        return new Filter[] {characterEncodingFilter};    }    @Override    protected void registerDispatcherServlet(ServletContext servletContext) {        super.registerDispatcherServlet(servletContext);       	servletContext.addListener(new RequestContextListener());    }        @Override    protected void customizeRegistration(ServletRegistration.Dynamic registration) {        registration.setInitParameter("defaultHtmlEscape", "true");        registration.setInitParameter("spring.profiles.active", "default");            	MultipartConfigElement multipartConfigElement = new MultipartConfigElement(        		"/VillageGreensMembers/documents", maxUploadSizeInMb, maxUploadSizeInMb * 2, maxUploadSizeInMb / 2);                registration.setMultipartConfig(multipartConfigElement);    }}