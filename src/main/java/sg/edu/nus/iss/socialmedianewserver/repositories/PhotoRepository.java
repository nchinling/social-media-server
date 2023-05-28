package sg.edu.nus.iss.socialmedianewserver.repositories;

import static sg.edu.nus.iss.socialmedianewserver.repositories.Queries.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoRepository {
    
    @Autowired
	private JdbcTemplate template;

    public void saveImage(String postId, String contentType, byte[] content) {
        template.update(
           SQL_SAVE_IMAGE, postId, contentType, content
        );
    }

}
