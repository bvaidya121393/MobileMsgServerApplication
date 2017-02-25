package com.example;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Credential;

@Service
@Transactional
public class MessageService {

//	@Autowired
//	CredentialRepository credentialRepository;
	
	@Autowired
	EntityManager entityManager;
	
	private final String FETCH_FIRST_AVAILABLE_DESTINATION = "select destinationId,mobNo from credentials where isAvailable=:isAvailable limit 1";
	
	private final String CREATE_AVAILABLE_DESTINATION = "insert into credentials(mobNo,createdOnDt,isAvailable) values (?,?,?)";
	
	private final String MAKE_DESTINATION_UNAVAILABLE = "update credentials set isAvailable='N' where destinationId=:destinationId";
	
	private final String MAKE_DESTINATION_AVAILABLE = "update credentials set isAvailable='Y' where mobNo=:mobNo";
	
	private final String FETCH_DESTINATION_BY_MOB_NO = "select destinationId from credentials where mobNo=:mobNo AND isAvailable='N'";
	
	private final String CHECK_DUPLICATE_MSG = "select publishingDate from messagelog where fk_destinationId=:destinationId AND message=:message order by publishingDate desc limit 1";
	
	private final String CREATE_MSG_LOG = "insert into messagelog(fk_destinationId,message,publishingDate) values (?,?,?)";
	
	@SuppressWarnings("unchecked")
	public CredentialMessage fetchCredentials() {
		// TODO Auto-generated method stub
		CredentialMessage credentialMsg = null;
		String query = FETCH_FIRST_AVAILABLE_DESTINATION;
		Query jpaQuery = entityManager.createNativeQuery(query);
		jpaQuery.setParameter("isAvailable", "Y");
		try{
			Object[] port =  (Object[]) jpaQuery.getSingleResult();
			if(port != null){
				credentialMsg = new CredentialMessage((String)port[1],(Integer)port[0]);
				jpaQuery = entityManager.createNativeQuery(MAKE_DESTINATION_UNAVAILABLE);
				jpaQuery.setParameter("destinationId", (Integer)port[0]);
				jpaQuery.executeUpdate();
			}
		}
		catch(Exception e){
			
		}
		return credentialMsg;
	}

	public Boolean createCredentials() {
		// TODO Auto-generated method stub
		try {
			Query query = entityManager.createNativeQuery(CREATE_AVAILABLE_DESTINATION);
			String mobileNumber = this.generateRandomNumber();
	        query.setParameter(1, mobileNumber);
	        query.setParameter(2, new Date());
	        query.setParameter(3, "Y");
	        query.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private String generateRandomNumber() {
		// TODO Auto-generated method stub
		long timeSeed = System.nanoTime();

        double randSeed = Math.random() * 1000;

        long midSeed = (long) (timeSeed * randSeed);

        String s = midSeed + "";
        String subStr = s.substring(0, 10);
        return subStr;
	}

	public Integer fetchByMobNo(String mobNo) {
		// TODO Auto-generated method stub
		Integer destinationId = null;
		String query = FETCH_DESTINATION_BY_MOB_NO;
		Query jpaQuery = entityManager.createNativeQuery(query);
		jpaQuery.setParameter("mobNo", mobNo);
		try{
			destinationId = (Integer) jpaQuery.getSingleResult();
		}
		catch(Exception e){
			
		}
		return destinationId;
	}

	public Boolean addToChatTranscript(Integer destinationId, IncomingMessage message) {
		// TODO Auto-generated method stub
		try {
			synchronized (this) {
				Date pubDate = null;
				String query = CHECK_DUPLICATE_MSG;
				Query jpaQuery = entityManager.createNativeQuery(query);
				jpaQuery.setParameter("destinationId", destinationId);
				jpaQuery.setParameter("message", message.getMessage());
				try{
					pubDate = (Date) jpaQuery.getSingleResult();
					Date currentDate = new Date();
					if(currentDate.getTime() - pubDate.getTime() <= 5000){
						return true;
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				Query createQuery = entityManager.createNativeQuery(CREATE_MSG_LOG);
				createQuery.setParameter(1, destinationId);
				createQuery.setParameter(2, message.getMessage());
				createQuery.setParameter(3, new Date());
				createQuery.executeUpdate();
		        return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Boolean revokeCredentials(String mobNo) {
		// TODO Auto-generated method stub
		Query jpaQuery = entityManager.createNativeQuery(MAKE_DESTINATION_AVAILABLE);
		jpaQuery.setParameter("mobNo", mobNo);
		jpaQuery.executeUpdate();
		return true;
	}

}
