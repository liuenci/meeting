package com.stylefeng.guns;

import com.stylefeng.guns.rest.OrderApplication;
import com.stylefeng.guns.core.util.FTPUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class OrderApplicationTests {

	@Autowired
	private FTPUtils ftpUtils;

	@Test
	public void contextLoads() {
		String fileStr = ftpUtils.getFileStrByAddress("seats/cgs.json");
		log.info("输出:{}", fileStr);
	}

}
