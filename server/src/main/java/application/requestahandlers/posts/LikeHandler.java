package application.requestahandlers.posts;

import api.ApiHandler;
import api.Response;
import application.datacomponents.like.Like;
import application.datamanagement.database.DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;

public class LikeHandler
{
    private ApiHandler apiHandler;

    public LikeHandler(ApiHandler apiHandler)
    {
        this.apiHandler = apiHandler;
    }

    public void addLike(Like like) throws SQLException
    {
        DatabaseManager.addLike(like);
    }

    public void deliverLikesQuantity(String post) throws IOException, SQLException
    {
        Response response = new Response(DatabaseManager.getLikesQuantity(post).toString());
        apiHandler.answerToClient(response);
    }

    public void deliverLikes(String post) throws IOException, SQLException
    {
        Response response = new Response(DatabaseManager.getLikes(post));
        apiHandler.answerToClient(response);
    }

    public void deliverIsNew(Like like) throws IOException, SQLException
    {
        if (DatabaseManager.isLikeNew(like))
        {
            Response isNew = new Response("true");
            apiHandler.answerToClient(isNew);
        }
        else
        {
            Response isNew = new Response("false");
            apiHandler.answerToClient(isNew);
        }
    }

    public void addDisLike(Like like) throws SQLException
    {
        DatabaseManager.addDislike(like);
    }
}
