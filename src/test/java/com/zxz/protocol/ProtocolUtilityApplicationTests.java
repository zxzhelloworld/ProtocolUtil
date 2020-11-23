package com.zxz.protocol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
class ProtocolUtilityApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		Process proc;
		try {
			String [] cmd=new String[]{"cmd", "/C", "D:\\py\\python.py","5","9"};
			proc = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			proc.waitFor();
			proc.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
