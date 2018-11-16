/**
 * @Author: Michael
 * @Date: Created in 19:50 2018/6/14
 * @Desciption:
 */

import com.example.demo.DemoApplication;
import com.example.demo.domain.HpbPushHistory;
import com.example.demo.repository.UserRepository;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class JPATest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() {
        List<HpbPushHistory> list =  userRepository.findByPushTo("1");
        System.out.println(list);
    }

}
