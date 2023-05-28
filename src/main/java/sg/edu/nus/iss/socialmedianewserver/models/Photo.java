package sg.edu.nus.iss.socialmedianewserver.models;

//Model is used for holding data retrieved from database, esp data for images 
public record Photo(String postId, String contentType,
    byte[] content){
    
}