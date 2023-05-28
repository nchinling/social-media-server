package sg.edu.nus.iss.socialmedianewserver.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.socialmedianewserver.repositories.PhotoRepository;
import sg.edu.nus.iss.socialmedianewserver.repositories.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepo;

    @Autowired
	private PhotoRepository photoRepo;

    @Transactional(rollbackFor = IOException.class)
	public String posts(String title, String comments, MultipartFile image) throws IOException {

		String postId = UUID.randomUUID().toString().substring(0, 8);
		
		// MySQL will rollback if MongoDB operation throws an exception
		photoRepo.saveImage(postId, image.getContentType(), image.getBytes());
		
		// MongoDB
		postRepo.insertPosts(postId, title, comments);

		return postId;

	}
}
