package uk.co.village_greens_coop.VillageGreensMemberPortal.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

public class Utils {

	public static byte[] getClassPathResourceIntoByteArray(String resourceName) throws IOException {
		InputStream is = new ClassPathResource(resourceName).getInputStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}

		buffer.flush();
		return buffer.toByteArray();
	}
}
