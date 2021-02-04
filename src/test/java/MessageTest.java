import java.io.InputStream;

import org.simplejavamail.outlookmessageparser.OutlookMessageParser;
import org.simplejavamail.outlookmessageparser.model.OutlookMessage;

public class MessageTest {

	public static void main(String[] args) {
		
		try {
			InputStream resourceAsStream = 
					OutlookMessageParser.class.getClassLoader().
					getResourceAsStream("simple email with TO and CC_single.msg");
			OutlookMessage msg = new OutlookMessageParser().parseMsg(resourceAsStream);
			System.out.println(msg.getBodyText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
