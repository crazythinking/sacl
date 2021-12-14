package net.engining.sacl.online2.autotest.cases;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import net.engining.sacl.online2.autotest.support.AbstractTestCaseTemplate;
import net.engining.sacl.online2.bus.User;
import net.engining.sacl.online2.bus.UserInput4AutoAckStreamHandler;
import net.engining.sacl.online2.bus.UserOutputStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-10-31 18:25
 * @since :
 **/
@ActiveProfiles(profiles = {
        "bus.disable",
        //"channel.stream.input.rabbit",
        "channel.stream.input.kafka",
        //"channel.stream.output.rabbit",
        "channel.stream.output.kafka",
        //"rabbit.dev",
        "kafka.dev",
        "channel.input.dev",
        "channel.output.dev",
})
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class Stream4AutoAckTestCase extends AbstractTestCaseTemplate {

    @Autowired
    UserOutputStreamHandler userMsgOutputStreamHandler;

    @Autowired
    UserInput4AutoAckStreamHandler userMsgInputStreamHandler;

    @Override
    public void initTestData() throws Exception {

    }

    @Override
    public void assertResult() throws Exception {

        //for autoAck, have one error
        Assert.isTrue(userMsgInputStreamHandler.okCount.get()==4, () -> "msg count != "+ 4);

    }

    @Override
    public void testProcess() throws Exception {
        autoAck();
    }

    private void autoAck() throws InterruptedException {
        User user = setupUser();
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("gender", user.getAge() % 2);
        int n = 5;
        for (int i = 0; i < n; i++) {
            if (i==n-1){
                user.setAge(0);
            }
            userMsgOutputStreamHandler.send(user, headerMap);
        }

        //等待另一个线程获取到消息并赋值
        Thread.sleep(20000);
    }

    private User setupUser() {
        User user = new User();
        user.setUserId(System.currentTimeMillis());
        user.setName("user1.name");
        user.setAge(RandomUtil.randomInt(1, 100));

        return user;
    }

    @Override
    public void end() throws Exception {

    }
}
