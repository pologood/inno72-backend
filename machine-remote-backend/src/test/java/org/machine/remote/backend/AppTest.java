package org.machine.remote.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.inno72.common.utils.StringUtil;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) {
		try {
			// read file content from file
			StringBuffer sb = new StringBuffer("");

			FileReader reader = new FileReader("/Users/lzh/a1.log");
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			int i = 0;
			while ((str = br.readLine()) != null) {
				String times = str.substring(0, 19);
				String id = str.substring(str.lastIndexOf("machineId：") + 10, str.lastIndexOf("machineId：") + 18);
				sb.append("insert into `inno72_test1`.`test` ( `id`, `time`, `machine_id`) values ( '")
						.append(StringUtil.uuid()).append("', '").append(times).append("', '").append(id).append("');");
				System.out.println(i++);
			}

			br.close();
			reader.close();
			FileWriter writer = new FileWriter("/Users/lzh/a1.sql");
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(sb.toString());

			bw.close();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
