package sg.edu.nus.iss.socialmedianewserver.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class LikesResponse {
    private String postId;
    private String totalPlusCount;
    private String totalMinusCount;

    public LikesResponse() {
    }

    
    public LikesResponse(String postId, String totalPlusCount, String totalMinusCount) {
        this.postId = postId;
        this.totalPlusCount = totalPlusCount;
        this.totalMinusCount = totalMinusCount;
    }

    public String getPostId() {
        return postId;
    }


    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getTotalPlusCount() {
        return totalPlusCount;
    }


    public void setTotalPlusCount(String totalPlusCount) {
        this.totalPlusCount = totalPlusCount;
    }


    public String getTotalMinusCount() {
        return totalMinusCount;
    }


    public void setTotalMinusCount(String totalMinusCount) {
        this.totalMinusCount = totalMinusCount;
    }


    public static LikesResponse createUserObjectFromRedis(String jsonStr) throws IOException{
        LikesResponse lr = new LikesResponse();

        System.out.printf(">>> inside createUserObjectFromRedis>>>>>\n");
        try(InputStream is = new ByteArrayInputStream(jsonStr.getBytes())) {
            JsonObject o = toJSON(jsonStr);
            lr.setPostId(o.getString("postId"));
            lr.setTotalPlusCount(o.getString("totalPlusCount"));
            lr.setTotalMinusCount(o.getString("totalMinusCount"));
        }
   
        return lr;
    }

    public static JsonObject toJSON(String json){
        System.out.printf(">>> inside toJson>>>>\n");
        JsonReader r = Json.createReader(new StringReader(json));
        return r.readObject();
    }

    public JsonObject toJSON(){
        return Json.createObjectBuilder()
                .add("postId", this.getPostId())
                .add("totalPlusCount", this.getTotalPlusCount())
                .add("totalMinusCount", this.getTotalMinusCount())
                .build();
    }


}
