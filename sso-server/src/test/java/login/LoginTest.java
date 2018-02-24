package login;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.tongwei.Const;
import com.tongwei.auth.model.User;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.SessionExUtil;

import base.BaseTest;

/**
 * @author		yangz
 * @date		2018年2月22日 下午4:03:35
 * @description	登录测试,压力测试
 */
public class LoginTest 
extends BaseTest
{
	
	@Test
	public void testLogin() throws Exception {
		System.out.println("开始");
		for (int i = 1; i < 100000; i++) {
			HttpUtil http = new HttpUtil();
			http.postFormHeader("http://localhost:8081/login", "loginName=yangz"+i+"&password=1234");
			System.err.println(i);
		}
		System.out.println("结束");
	}
	
	@Autowired
	IUserService userService;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Test
	public void testCreateAdminUser() throws Exception {
		for (int i = 20001; i <= 100000; i++) {
			User user = new User();
			user.setCreateTime(new Date());
			user.setLoginName("yangz"+i);
			user.setRealName("yali");
			user.setCellPhone(UUID.randomUUID().toString());
			user.setPassword("301DE9290755171627162B065DFE425F");
			userService.save(user);
			
			roleMapper.saveUserRole(user.getId(), 1);
		}
	}
	
	@Test
	public void testYalices() throws Exception {
		Runnable task = new Runnable() {
			public void run() {
				for (int i = 200; i < 100000; i++) {
					String[] sidsFromMap = SessionExUtil.getSidsFromMap(i);
					if(sidsFromMap==null){
						continue;
					}
					BasicClientCookie cookie = new BasicClientCookie("SESSION", sidsFromMap[0]);
					cookie.setPath("/");
					cookie.setDomain("127.0.0.1");
					HttpUtil http = new HttpUtil(cookie);
					
					http.getJsonHeader("http://127.0.0.1:8081/v");
				}
			}
		};
		
		ExecutorService service = Executors.newFixedThreadPool(50);
		for (int i = 0; i < 30; i++) {
			service.submit(task);
			System.err.println("task "+i+"   ---start");
		}
		
		Thread.sleep(10000000000L);
	}
	
	@Autowired
	StringRedisTemplate redis;
	
	@Test
	public void testCheckLogQueueSize() throws Exception {
		BoundListOperations<String, String> ops = redis.boundListOps(Const.QUEUE_LOG_KEY);
		Long size = ops.size();
		System.err.println(size);
		
	}
	
	@Test
	public void testAddConsumer() throws Exception {
		Thread.sleep(1000000000L);
	}

}
