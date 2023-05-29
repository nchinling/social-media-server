package sg.edu.nus.iss.socialmedianewserver.repositories;

import java.io.IOException;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.socialmedianewserver.models.LikesResponse;
import sg.edu.nus.iss.socialmedianewserver.services.PostService;

@Component
public class KeyExpirationListener implements ApplicationListener<RedisKeyExpiredEvent> {

    //autowired in a bean.
    @Autowired @Qualifier("likesbean")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private PostService postSvc;
    
    @Override
    public void onApplicationEvent(RedisKeyExpiredEvent event) {
        String expiredKey = event.getSource().toString();
        String expiredValue = redisTemplate.opsForValue().get(expiredKey);

        // Parse the JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(expiredValue);
        // Extract the value of `totalPlusCount` as a string
        String totalPlusCountStr = jsonNode.get("totalPlusCount").asText();
        String totalMinusCountStr = jsonNode.get("totalMinusCount").asText();
        
        // Convert the string to an integer
        int totalPlusCount = Integer.parseInt(totalPlusCountStr);
        int totalMinusCount = Integer.parseInt(totalMinusCountStr);
            
        
            /*
            * db.posts.updateOne(
            * { _id: expiredKey},
            * {$set:{totalPlusCount: totalPlusCount, totalMinusCount: totalMinusCount}}
            * )
            */
            Query query = Query.query(
                Criteria.where("_id").is(expiredKey)
            );

            Update updateOps = new Update()
                .set("totalPlusCount", totalPlusCount)
                .set("totalMinusCount", totalMinusCount);
            mongoTemplate.updateFirst(
                query, updateOps, Document.class, "post"
            );

        } catch (JsonMappingException e) {
           
            e.printStackTrace();
        } catch (JsonProcessingException e) {
          
            e.printStackTrace();
        }

    }
}
