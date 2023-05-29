package sg.edu.nus.iss.socialmedianewserver.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.socialmedianewserver.models.LikesResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Repository
public class PostRepository {
    

    //autowired in a bean.
    @Autowired @Qualifier("likesbean")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;


    /*
	 * db.posts.insert({
	 * 	_id: postId,
	 * 	title: "title",
	 * 	content: "content",
	 * })
	 */
    public void insertPosts(String postId, String title, String comments){
        Document doc = new Document();
        doc.put("_id", postId);
        doc.put("title", title);
        doc.put("content", comments);

        mongoTemplate.insert(doc, "post");
    }

    //Redis
    public void save(LikesResponse likes){
        int duration = 4;
        Duration expireTime  = Duration.ofMinutes(duration);
        this.redisTemplate.opsForValue().set(likes.getPostId(), 
                            likes.toJSON().toString(), expireTime);
    }

    //Redis
    public Optional<LikesResponse> findByPostId(String postId) throws IOException{
        String json = redisTemplate.opsForValue().get(postId);
        if(null == json|| json.trim().length() <= 0){
            return Optional.empty();
        }
    
        System.out.printf(">>> inside postRepofindPost\n");
        
        //creates a LikesResponse object. 
        return Optional.of(LikesResponse.createUserObjectFromRedis(json));
    }


}
