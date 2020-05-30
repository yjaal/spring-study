package win.iot4yj.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import win.iot4yj.entity.User;
import win.iot4yj.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void batchInsertTest() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("003");
        user1.setName("泥巴-003");
        user1.setAge(18);
        user1.setSex("1");
        user1.setCardNo("001");

        User user2 = new User();
        user2.setId("004");
        user2.setName("yj-004");
        user2.setAge(29);
        user2.setSex("0");
        user2.setCardNo("002");

        users.add(user1);
        users.add(user2);

        boolean res = userService.saveBatch(users, 10);
        Assert.assertTrue(res);
    }

    @Test
    public void batchUpdateTest() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("001");
        user1.setName("泥巴-test");
        user1.setAge(18);
        user1.setSex("1");
        user1.setCardNo("001");

        User user2 = new User();
        user2.setId("002");
        user2.setName("yj-test");
        user2.setAge(29);
        user2.setSex("0");
        user2.setCardNo("002");

        users.add(user1);
        users.add(user2);
        boolean res = userService.updateBatchById(users);
        Assert.assertTrue(res);
    }

    @Test
    public void getPageTest() {
        List<User> users = userService.getPage(1, 2, null);
        Assert.assertEquals(0, users.size());
    }

    @Test
    public void getUserById() {
        User user = userService.getUserById("00424556");
        Assert.assertEquals("yj", user.getName());
    }
}