package sg.edu.nus.iss.socialmedianewserver.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import static sg.edu.nus.iss.socialmedianewserver.repositories.Queries.*;

@Repository
public class PostRepository {
    
    @Autowired
    MongoTemplate template;

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

        template.insert(doc, "post");
    }
}
