package test;

import java.util.List;

import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.AccountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/15 2:56
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:applicationContext-service.xml"
})
public class MybatisSpringTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testMybatisSpring() {
        List<Account> accountList = accountService.queryAccountList();
        accountList.forEach(System.out::println);
        Assert.assertNotNull(accountList);
    }
}
