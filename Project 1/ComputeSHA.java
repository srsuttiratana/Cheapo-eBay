import java.io.*;
import java.security.*;

public class ComputeSHA {

	public static void main(String[] args) throws Exception {
	MessageDigest message_digest = MessageDigest.getInstance("SHA-1");
        FileInputStream file_input_stream = new FileInputStream(args[0]);
        
        byte[] dataBytes = new byte[1024];
     
        int num_read = 0; 
        while ((num_read = file_input_stream.read(dataBytes)) != -1) {
          message_digest.update(dataBytes, 0, num_read);
        };
        byte[] message_digest_bytes = message_digest.digest();
     
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < message_digest_bytes.length; i++) {
          sb.append(Integer.toString((message_digest_bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println(sb.toString());
	}
}
