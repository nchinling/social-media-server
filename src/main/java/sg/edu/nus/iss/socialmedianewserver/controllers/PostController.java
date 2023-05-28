package sg.edu.nus.iss.socialmedianewserver.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.socialmedianewserver.models.Photo;
import sg.edu.nus.iss.socialmedianewserver.repositories.PhotoRepository;
import sg.edu.nus.iss.socialmedianewserver.repositories.PostRepository;
import sg.edu.nus.iss.socialmedianewserver.services.PostService;

@Controller
@RequestMapping
@CrossOrigin(origins="*")
public class PostController {
    
    @Autowired
    private PostService postSvc;

	@PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<String> postUpload(@RequestPart String title, 
                                            @RequestPart String comments,
			                    @RequestPart MultipartFile imageFile) {

    String postId = null;
    JsonObject resp = null;
      
		System.out.printf(">>> title: %s\n", title);
        System.out.printf(">>> comments: %s\n", comments);
		System.out.printf(">>> filename: %s\n", imageFile.getOriginalFilename());

		try {
            postId = postSvc.posts(title, comments, imageFile);
            resp = Json.createObjectBuilder()
            .add("postId", postId)
            .add("title", title)
            .add("comments", comments)
            .build();
            
            // return ResponseEntity.ok(resp.toString());
    
		} catch (IOException ex) {
			ex.printStackTrace();
		}
        
        return ResponseEntity.ok(resp.toString());

		// return ResponseEntity.ok("{}");
	}
}
