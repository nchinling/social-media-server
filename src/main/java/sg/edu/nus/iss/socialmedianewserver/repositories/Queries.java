package sg.edu.nus.iss.socialmedianewserver.repositories;

public class Queries {

	public static final String SQL_SAVE_IMAGE = """
			insert into post(post_id, image_type, image) values (?, ?, ?)
		""";

    public static final String SQL_GET_IMAGE_BY_ID = """
        
    select * from posts where post_id = ?

    """;
}
