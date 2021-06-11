package application.datamanagement.database;

import application.util.User;
import application.util.followerfollowing.FollowerFollowingPack;
import application.util.search.SearchResult;

import java.sql.*;


public class DatabaseManager
{
    private final String URL_CONNECTION = "jdbc:mysql://localhost:3306/jdbc?user=root";
    private final Connection CONNECTION = DriverManager.getConnection(URL_CONNECTION);

    public DatabaseManager() throws SQLException {}

    public void addUserRecord(User user) throws SQLException
    {
        String insertQuery = "INSERT INTO users(username,password,email,bio)" +
                " VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = CONNECTION.prepareStatement(insertQuery);

        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());
        if (user.getBio() != null)
        {
            preparedStatement.setString(4,user.getBio());
        }
        else
        {
            preparedStatement.setString(4,"");
        }

        preparedStatement.execute();

        preparedStatement.close();
    }

    public boolean checkExistence(String usernameToCheck) throws SQLException
    {
        String searchQuery = "SELECT username FROM users " +
                "WHERE username = ?";
        PreparedStatement preparedStatement = CONNECTION.prepareStatement(searchQuery);
        preparedStatement.setString(1,usernameToCheck);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean result;
        if (!resultSet.next())
        {
            result = false;
        }
        else
        {
            result = true;
        }

        preparedStatement.close();

        return result;
    }

    public void setFollowerFollowing(FollowerFollowingPack pack) throws SQLException
    {
        String query1;
        String query2;
        PreparedStatement statement1;
        PreparedStatement statement2;

        if (pack.isForUnfollow())
        {
            query1 = "DELETE FROM followers " +
                    "WHERE username = ?";
            query2 = "DELETE FROM followings " +
                    "WHERE username = ?";

            statement1 = CONNECTION.prepareStatement(query1);
            statement2 = CONNECTION.prepareStatement(query2);

            statement1.setString(1,pack.getUsername());
            statement2.setString(1,pack.getFollower());
        }
        else
        {
            query1 = "INSERT INTO followers(username,follower)" +
                    " VALUES (?, ?)";
            query2 = "INSERT INTO followings(username,following)" +
                    " VALUES (?, ?)";

            statement1 = CONNECTION.prepareStatement(query1);
            statement2 = CONNECTION.prepareStatement(query2);

            statement1.setString(1,pack.getUsername());
            statement1.setString(2,pack.getFollower());
            statement2.setString(1,pack.getFollower());
            statement2.setString(2,pack.getUsername());
        }

        statement1.execute();
        statement2.execute();

        statement1.close();
        statement2.close();
    }

    public SearchResult getSearchResult(String usernameToManipulate) throws SQLException
    {
        if (checkExistence(usernameToManipulate))
        {
            SearchResult searchResult = new SearchResult();

            String getFollowersQuery = "SELECT * FROM followers " +
                    "WHERE username = ?";
            PreparedStatement statement1 = CONNECTION.prepareStatement(getFollowersQuery);
            statement1.setString(1,usernameToManipulate);
            ResultSet followersSet = statement1.executeQuery();
            while (followersSet.next())
            {
                searchResult.addFollowers(followersSet.getString("follower"));
            }
            statement1.close();

            String getFollowingsQuery = "SELECT * FROM followings " +
                    "WHERE username = ?";
            PreparedStatement statement2 = CONNECTION.prepareStatement(getFollowingsQuery);
            statement2.setString(1,usernameToManipulate);
            ResultSet followingsSet = statement2.executeQuery();
            while (followingsSet.next())
            {
                searchResult.addFollowings(followingsSet.getString("following"));
            }
            statement2.close();

            String getUser = "SELECT * FROM users " +
                    "WHERE username = ?";
            PreparedStatement statement3 = CONNECTION.prepareStatement(getUser);
            statement3.setString(1,usernameToManipulate);
            ResultSet userSet = statement3.executeQuery();
            searchResult.setUsername(usernameToManipulate);
            while (userSet.next())
            {
                searchResult.setEmail(userSet.getString("email"));
                searchResult.setBio(userSet.getString("bio"));
            }
            statement3.close();

            return searchResult;
        }
        else
        {
            return null;
        }
    }
}