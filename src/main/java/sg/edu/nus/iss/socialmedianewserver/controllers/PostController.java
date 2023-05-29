package sg.edu.nus.iss.socialmedianewserver.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.socialmedianewserver.models.LikesResponse;

import sg.edu.nus.iss.socialmedianewserver.services.PostService;

@Controller
@RequestMapping(path="/")
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
             //save initial vote count
            LikesResponse firstlike = new LikesResponse(postId, "0", "0");
            postSvc.save(firstlike);
            
            // return ResponseEntity.ok(resp.toString());
    
		} catch (IOException ex) {
			ex.printStackTrace();
		}
        
        return ResponseEntity.ok(resp.toString());

		// return ResponseEntity.ok("{}");
	}


	@PostMapping(path="/likes", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> postUploadLikes(@RequestBody String payload) {

        System.out.printf(">>> in postUploadLikes\n");
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject req = reader.readObject();
        String postId = req.getString("postId");
        int plusCount = req.getInt("plusCount");
        int minusCount = req.getInt("minusCount");
        JsonObject resp = null;

        Optional<LikesResponse> likesResponse;
        try {
            likesResponse = postSvc.findByPostId(postId);
            LikesResponse redisLikes = likesResponse.get();
            Integer totalPlusCount = Integer.parseInt(redisLikes.getTotalPlusCount())
                + plusCount;
            Integer totalMinusCount = Integer.parseInt(redisLikes.getTotalMinusCount())
                + minusCount;
            redisLikes.setTotalPlusCount(totalPlusCount.toString());
            redisLikes.setTotalMinusCount(totalMinusCount.toString());
            postSvc.save(redisLikes);
            System.out.printf(">>> totalPlusCount: %s\n", totalPlusCount);
            resp = Json.createObjectBuilder()
            .add("postId", postId)
            .add("totalPlusCount", totalPlusCount)
            .add("totalMinusCount", totalMinusCount)
            .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
      
        return ResponseEntity.ok(resp.toString());
	}


}
