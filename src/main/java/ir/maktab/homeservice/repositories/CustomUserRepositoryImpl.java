package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    EntityManager entityManager;

    List<String> validParameters = Arrays.asList("size"  ,"page" , "firstName" , "lastName" , "email" , "role" , "assistance");
    List<String> validReportParameters  = Arrays.asList("page" ,"size" , "before" , "after" , "donecount" , "receivedcount");

    @Override
    public List<User> getReport(Map<String, String> parameterMap) {
        if (!validReportParameters.containsAll(parameterMap.keySet()))
            throw new IllegalArgumentException();
        String queryString = "SELECT u FROM User u ";
        Set<String> queryParams = getParams(parameterMap);
        String conditions = queryParams.stream().collect(Collectors.joining(" AND "));
        if (queryParams.size()!=0)
            queryString+="WHERE " + conditions;
        System.out.println(queryString);
        Query query = entityManager.createQuery(queryString);
        try {
            setParams(query , parameterMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parameterMap.containsKey("page")&& parameterMap.containsKey("size")){
            int page = Integer.parseInt(parameterMap.get("page"));
            int size = Integer.parseInt(parameterMap.get("size"));
            query.setFirstResult((page-1) * size);
            query.setMaxResults(page);
        }
        return query.getResultList();
    }

    private void setParams(Query query, Map<String, String> parameterMap) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        if(parameterMap.containsKey("after")){
            query.setParameter("after" , formatter.parse(parameterMap.get("after")));
        }
        if(parameterMap.containsKey("before")){
            query.setParameter("before" , formatter.parse(parameterMap.get("before")));
        }
        if (parameterMap.containsKey("donecount")){
            query.setParameter("donecount" , Long.valueOf(parameterMap.get("donecount")));
        }
        if (parameterMap.containsKey("receivedcount")){
            query.setParameter("receivedcount", Long.valueOf(parameterMap.get("receivedcount")));
        }
    }

    private Set<String> getParams(Map<String, String> parameterMap){
        Set<String> queryParams = new HashSet<>();
        if(parameterMap.containsKey("after")){
            queryParams.add("u.registrationDate >= :after");
        }
        if(parameterMap.containsKey("before")){
            queryParams.add("u.registrationDate <= :before");
        }
        if (parameterMap.containsKey("donecount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.selectedOffer.specialist=u)>=:donecount");
        }
        if (parameterMap.containsKey("receivedcount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.customer=u)>=:receivedcount");
        }
        return queryParams;
    }
























    @Override
    public List<User> findByParameters(Map<String, String> parameterMap) {
        if (!validParameters.stream().map(String::toLowerCase).collect(Collectors.toList())
                .containsAll(parameterMap.keySet().stream().map(String::toLowerCase).collect(Collectors.toList())) )
            throw new IllegalArgumentException("parameters are not valid.");
        String queryString = createQueryFromParameters(parameterMap);
        Query query = entityManager.createQuery(queryString);
        System.out.println(queryString);
        if (parameterMap.containsKey("page")&& parameterMap.containsKey("size")){
            int page = Integer.parseInt(parameterMap.get("page"));
            int size = Integer.parseInt(parameterMap.get("size"));
            query.setFirstResult((page-1) * size);
            query.setMaxResults(page);
        }
        return query.getResultList();
    }

    private String createQueryFromParameters(Map<String, String> parameterMap){
        String query = "SELECT u FROM User u";
        List<String> parameters=new ArrayList<>();
        if (parameterMap.containsKey("assistance")) {
            query = "SELECT u FROM Specialist u";
            String assistance = parameterMap.get("assistance");
            parameters.add(assistance +" member of u.assistances");
        }

        for (Map.Entry<String, String> entry : parameterMap.entrySet()){
            if (entry.getKey().equals("page") ||entry.getKey().equals("size") || entry.getKey().equals("assistance") )
                continue;
            String parameter = validParameters.stream().filter(p->p.toLowerCase().equals(entry.getKey().toLowerCase())).findFirst().get();
            parameters.add("u." + parameter + "='" + entry.getValue() +"'");
        }
        System.out.println(parameters.size());
        if (!parameters.isEmpty()){
            System.out.println("not empty");
            query+= " WHERE " + parameters.stream().collect(Collectors.joining(" AND "));
        }
        return query;
    }
}
