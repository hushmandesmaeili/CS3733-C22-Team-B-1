package edu.wpi.cs3733.c22.teamB.entity.MongoDB;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import edu.wpi.cs3733.c22.teamB.entity.objects.Location;


import java.net.UnknownHostException;
import java.util.List;

public class MongoDB {

    public static MongoClient mongoClient;
    public static MongoDatabase BDBMongo;

    public static void getConnection()  {

        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:admin@softengteam.cb0wu.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        mongoClient = MongoClients.create(settings);

    }

    public static DBObject convert(Location location){
        return new BasicDBObject("nodeID", location.getNodeID()).append("xcoord", location.getXcoord()).append("ycoord", location.getYcoord()).append("floor", location.getFloor()).append("building", location.getBuilding()).append("nodeType", location.getNodeType()).append("longName", location.getLongName()).append("shortName", location.getShortName());
    }
    public static MongoDatabase getBDBMongo() {
        BDBMongo = mongoClient.getDatabase("BDBMongo");
        return BDBMongo;
    }

    public static void closeConnection() {
        mongoClient.close();
    }
}
