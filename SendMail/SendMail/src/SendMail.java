import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SendMail {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "context.xml" });
		BeanFactory factory = (BeanFactory) appContext;

		System.out.println("Sending mail...");
		MailSenderServiceImpl svc = (MailSenderServiceImpl) factory.getBean("mailSenderService");
		List<String> mails = new ArrayList<String>();
		for (String mail : mails) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\t==>" + mail);
			Map<String, String> model = new HashMap<String, String>();
			svc.sendEmail(new String[] { mail },
					"Batimat optimisez vos déplacements, transferts et évènements avec Transeo", model, "mail.html");
		}

		System.out.println("Mail sent");
	}
}
