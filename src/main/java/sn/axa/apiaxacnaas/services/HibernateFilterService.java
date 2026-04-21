package sn.axa.apiaxacnaas.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.util.RoleEnum;

@Service
@Transactional
public class HibernateFilterService {
    @PersistenceContext
    private EntityManager entityManager;

    public void enablePartnerFilter(User currentUser) {
        Session session = entityManager.unwrap(Session.class);
        if(currentUser==null || currentUser.getRole().getName().equals(RoleEnum.SUPER_ADMIN)){
            return;
        }
        if(currentUser.getPartner().getId()!=null){
            session.enableFilter("partnerFilter").setParameter("partnerId", currentUser.getPartner().getId());
        }
    }

    public void enableUserFilter(User currentUser) {
        Session session = entityManager.unwrap(Session.class);
        if(currentUser==null || currentUser.getRole().getName().equals(RoleEnum.SUPER_ADMIN)){
            return;
        }
        if(currentUser.getId()!=null){
            session.enableFilter("userFilter").setParameter("userId", currentUser.getId());
        }


    }

}
