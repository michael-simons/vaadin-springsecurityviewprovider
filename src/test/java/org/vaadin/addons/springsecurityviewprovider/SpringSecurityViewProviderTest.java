/*
 * Copyright 2013 ENERKO Informatik GmbH
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
 * THIS SOFTWARE IS  PROVIDED BY THE  COPYRIGHT HOLDERS AND  CONTRIBUTORS "AS IS"
 * AND ANY  EXPRESS OR  IMPLIED WARRANTIES,  INCLUDING, BUT  NOT LIMITED  TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL  THE COPYRIGHT HOLDER OR CONTRIBUTORS  BE LIABLE
 * FOR ANY  DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR  CONSEQUENTIAL
 * DAMAGES (INCLUDING,  BUT NOT  LIMITED TO,  PROCUREMENT OF  SUBSTITUTE GOODS OR
 * SERVICES; LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT  LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE  USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.vaadin.addons.springsecurityviewprovider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.vaadin.addons.springsecurityviewprovider.views.CacheableTestView;
import org.vaadin.addons.springsecurityviewprovider.views.RestrictedView1;
import org.vaadin.addons.springsecurityviewprovider.views.RestrictedView2;
import org.vaadin.addons.springsecurityviewprovider.views.UncacheableTestView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

/**
 * @author Michael J. Simons, 2013-04-03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@ActiveProfiles({"test"})
public class SpringSecurityViewProviderTest {
	@Configuration
	@Profile("test")
	@ComponentScan("org.vaadin.addons.springsecurityviewprovider.views")
	@ImportResource({
		"classpath:/org/vaadin/addons/springsecurityviewprovider/config/security.xml"
	})	
	@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.ENABLED)
	@EnableSpringConfigured
	@EnableAspectJAutoProxy
	public static class TestConfiguration {	
	}

	final static Authentication anonymousUser = new AnonymousAuthenticationToken("anonymousUser", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
	final static Authentication testUser = new TestingAuthenticationToken("testUser", "testUser", "authenticatedUser");
	final static Authentication superUser = new TestingAuthenticationToken("superUser", "superUser", AuthorityUtils.createAuthorityList("authenticatedUser", "superUser"));
		
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void shouldCacheCacheableViews() {		
		final ViewProvider viewProvider = SpringSecurityViewProvider.createViewProvider(anonymousUser, true);
		final View v1 = viewProvider.getView(CacheableTestView.VIEW_NAME);
		final View v2 = viewProvider.getView(CacheableTestView.VIEW_NAME);		
		Assert.assertEquals(v1, v2);		
	}

	@Test
	public void shouldNotCacheUncacheableViews() {
		final ViewProvider viewProvider = SpringSecurityViewProvider.createViewProvider(anonymousUser, true);
		final View v1 = viewProvider.getView(UncacheableTestView.VIEW_NAME);
		final View v2 = viewProvider.getView(UncacheableTestView.VIEW_NAME);		
		Assert.assertNotEquals(v1, v2);
	}
	
	@Test	
	public void shouldNotFailOnUnknownView() {
		final ViewProvider viewProvider = SpringSecurityViewProvider.createViewProvider(anonymousUser, true);
		viewProvider.getView("someUnknownView");
	}
		
	@Test
	public void restrictedViewsShouldNotBeInstantiated() {		
		ViewProvider viewProvider = SpringSecurityViewProvider.createViewProvider(anonymousUser, true);		
		Assert.assertNull(viewProvider.getView(RestrictedView1.VIEW_NAME));
		
		viewProvider = SpringSecurityViewProvider.createViewProvider(testUser, true);		
		Assert.assertNull(viewProvider.getView(RestrictedView1.VIEW_NAME));
	}
	
	@Test
	public void permissionsShouldBeEvaluated() {
		ViewProvider viewProvider = SpringSecurityViewProvider.createViewProvider(superUser, true);		
		Assert.assertNotNull(viewProvider.getView(RestrictedView2.VIEW_NAME));
	}
}