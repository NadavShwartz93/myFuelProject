package emServer;

import javax.mail.MessagingException;

public class SendMail implements Runnable {
	
	private String recepientMailAddress; 
	private String mailTitle; 
	private String mailBody;
	
	/**This constructor send the mail to the STATION_MANAGER to announce about new fuel order for the  inventory.
	 * @param recepientMailAddress
	 * @param mailTitle
	 * @param mailBody
	 */
	public SendMail(String recepientMailAddress, String mailTitle, String mailBody){
		this.recepientMailAddress = recepientMailAddress;
		this.mailTitle = mailTitle;
		this.mailBody = mailBody;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
						try {
			JavaMailUtil.sendMail(recepientMailAddress,mailTitle,mailBody);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
			}
		});
		t1.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
