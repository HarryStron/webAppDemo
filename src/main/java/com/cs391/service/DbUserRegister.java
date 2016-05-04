package com.cs391.service;

import com.cs391.data.Administrator;
import com.cs391.data.Credentials;
import com.cs391.data.UserGroup;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class DbUserRegister {
    
    @PersistenceContext(unitName = "WebappsDB")
    private EntityManager em;
    
    @PostConstruct
    private void register() {
//        try {
//            try{
//                em.remove(em.merge(em.createQuery("SELECT a FROM Administrator a").getResultList()));
//                em.remove(em.merge(em.createQuery("SELECT c FROM Credentials c").getResultList()));
//                em.remove(em.merge(em.createQuery("SELECT u FROM UserGroup u").getResultList()));
//            } catch (Exception e){}
//            
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update("admin1".getBytes("UTF-8"));
//            byte[] digest = md.digest();
//            BigInteger bigInt = new BigInteger(1, digest);
//            String paswdToStoreInDB = bigInt.toString(16);
//
//            Administrator admin = new Administrator();
//            admin.setSussexID("admin1");
//            admin.setName("admin");
//            admin.setSurname("admin");
//            admin.setEmail("admin1@sussex.ac.uk");
//            admin.setPhoneNum("0123456789");
//            em.persist(admin);
//            
//            Credentials credentials = new Credentials();
//            credentials.setSussexID("admin1");
//            credentials.setPass(paswdToStoreInDB);
//            em.persist(credentials);
//
//            UserGroup userGroup = new UserGroup();
//            userGroup.setSussexID("admin1");
//            userGroup.setGroupName("administrator");
//            em.persist(userGroup);
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            System.out.println("Failed to register new user");
//        }
    }
}
