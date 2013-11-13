import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author thai.phan
 */

@Service
public class MailSenderServiceImpl {
	private String defaultFrom;
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;

	/**
	 * Send an email
	 * 
	 * @param aTo
	 *            Destination email
	 * @param aSubject
	 *            Mail subject
	 * @param aModel
	 *            Data to display in the template
	 * @param aTemplateName
	 *            Template file name in the classpath
	 */
	@SuppressWarnings("rawtypes")
	public void sendEmail(final String[] aTo, final String aSubject, final Map aModel, final String aTemplateName) {
		sendEmail(aTo, null, aSubject, aModel, aTemplateName, null);
	}

	/**
	 * Send an email
	 * 
	 * @param aTo
	 *            Destination email
	 * @param aFrom
	 *            Sender email
	 * @param aSubject
	 *            Mail subject
	 * @param aModel
	 *            Data to display in the template
	 * @param aTemplateName
	 *            Template file name in the classpath
	 */
	@SuppressWarnings("rawtypes")
	public void sendEmail(final String[] aTo, final String aFrom, final String aSubject, final Map aModel,
			final String aTemplateName) {
		sendEmail(aTo, aFrom, aSubject, aModel, aTemplateName, null);
	}

	/**
	 * Send an email
	 * 
	 * @param aTo
	 *            Destination email
	 * @param aFrom
	 *            Sender email
	 * @param aSubject
	 *            Mail subject
	 * @param anAttacheFilePath
	 *            Attache file path
	 */
	public void sendEmail(final String[] aTo, final String aFrom, final String aSubject, final String anAttacheFilePath) {
		sendEmail(aTo, aFrom, aSubject, null, null, anAttacheFilePath);
	}

	/**
	 * Send an email
	 * 
	 * @param aTo
	 *            Destination email
	 * @param aSubject
	 *            Mail subject
	 * @param anAttacheFilePath
	 *            Attache file path
	 */
	public void sendEmail(final String[] aTo, final String aSubject, final String anAttacheFilePath) {
		sendEmail(aTo, null, aSubject, null, null, anAttacheFilePath);
	}

	public void sendEmail(String[] aTo, String aSubject, Map aModel, String aTemplateName, String anAttacheFilePath) {
		sendEmail(aTo, null, aSubject, aModel, aTemplateName, anAttacheFilePath);
	}

	public void sendEmailWithFiles(final String[] aTo, final String aFrom, final String aSubject,
			final String[] someFilesToAttach) {
		sendEmailWithFiles(aTo, aFrom, aSubject, null, null, someFilesToAttach);
	}

	public void sendEmailWithFiles(final String[] aTo, final String aSubject, final String[] someFilesToAttach) {
		sendEmailWithFiles(aTo, null, aSubject, null, null, someFilesToAttach);
	}

	@SuppressWarnings("rawtypes")
	private void sendEmail(final String[] aTo, final String aFrom, final String aSubject, final Map aModel,
			final String aTemplateName, final String anAttacheFileName) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(aTo);
				if (StringUtils.isNotBlank(aFrom)) {
					message.setFrom(aFrom);
				} else {
					message.setFrom(defaultFrom);
				}
				if (StringUtils.isNotBlank(aTemplateName)) {
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, aTemplateName, aModel);
					message.setText(text, true);
				}
				if (StringUtils.isNotBlank(anAttacheFileName)) {
					File file = new File(anAttacheFileName);
					FileSystemResource fileRessource = new FileSystemResource(file);
					message.addAttachment(file.getName(), fileRessource);
					if (StringUtils.isBlank(aTemplateName)) {
						message.setText("");
					}
				}

				if (StringUtils.isNotBlank(aSubject)) {
					message.setSubject(aSubject);
				}
			}
		};
		this.mailSender.send(preparator);
	}

	@SuppressWarnings("rawtypes")
	private void sendEmailWithFiles(final String[] aTo, final String aFrom, final String aSubject, final Map aModel,
			final String aTemplateName, final String[] someFilesToAttach) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(aTo);
				if (StringUtils.isNotBlank(aFrom)) {
					message.setFrom(aFrom);
				} else {
					message.setFrom(defaultFrom);
				}
				if (StringUtils.isNotBlank(aTemplateName)) {
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, aTemplateName, aModel);
					message.setText(text, true);
				}
				if (someFilesToAttach != null && someFilesToAttach.length > 0) {
					for (int i = 0; i < someFilesToAttach.length; i++) {
						File file = new File(someFilesToAttach[i]);
						FileSystemResource fileRessource = new FileSystemResource(file);
						message.addAttachment(file.getName(), fileRessource);
					}
					if (StringUtils.isBlank(aTemplateName)) {
						message.setText("");
					}
				}

				if (StringUtils.isNotBlank(aSubject)) {
					message.setSubject(aSubject);
				}
			}
		};
		this.mailSender.send(preparator);
	}

	// =================================================================================================================

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setDefaultFrom(String defaultFrom) {
		this.defaultFrom = defaultFrom;
	}
}
