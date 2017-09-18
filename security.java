package com.sdev425.everthome;

import java.util.Calendar;
import java.util.Random;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
/**
 *
 * @author Douglas Evert
 */
public interface security {
    //Creates the pin and e-mailing of the multi-factor setup IA-2(1) Identification and Authentication(Organizational Users)
    public class multiFactor{
        String answer;
        private int code; 
        
        public void sendMessage(){
            Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("sdev425evert","P@$$w0rd01");
				}
			});

		
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sdev425evert@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("sdev425evert@gmail.com"));
			message.setSubject("Multi Factor Login Code");
			message.setText("New Passcode: " + setCode() + "\nTime Stamp: " +  Calendar.getInstance().getTime() + " ");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
         
        private int setCode(){
            Random rand = new Random();
            code = rand.nextInt(9999) + 1000;
            return code;
        }
        
        public int getCode(){
            return code;
        }

    }
    
   //Creates a log file located in the root directory of the application (AU-3 Content of Audit Records)
    public class LogFile{
       
       public void logWriter(String msg){
           BufferedWriter writer = null; 
           try {
               writer = new BufferedWriter(new FileWriter("Log.txt", true));
               //writes messages to log and adds time stamps to log entries (AU-8 Time Stamps)
               writer.write(msg + Calendar.getInstance().getTime());
               writer.newLine();
           }catch(IOException io){
               System.out.println("Error " + io.getMessage());
           }finally {
               try {
                   if (writer != null) {
                       writer.close();
                   }
               }catch (IOException io){
                   System.out.println("Error closing file: " + io.getMessage());
               }
           }
       }
    }
    
    //Tracks login attempts and handles consent to monitoring message AC-7 (Unsuccessful Logon Attempts), AC-8 (System Use Notifications) 
    public class LoginTracking{
        int count; 
        public void unsuccsessfulLogin(){
            count++;
        }
        
        public void successfulLogin(){
            count = 0;
            //Displays a pop up window that acts as a system notification (AC-8) it does that after the computer recognizes a successful login
            JOptionPane.showMessageDialog(null, "You are accessing a system that is monitored"
                    + "\ncontining to use this system you are giving your "
                    + "\nconsent to monitor","Consent to Monitor", JOptionPane.INFORMATION_MESSAGE);
        }
        
        public boolean locked(){
            if (count >= 5){
                return true;
            }else{
                return false;
            }
        }   
    }
    
}        

