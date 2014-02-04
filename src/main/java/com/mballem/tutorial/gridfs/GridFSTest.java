package com.mballem.tutorial.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Marcio Ballem
 * Date: 31/01/14
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class GridFSTest {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient("localhost", 27017);

        DB db = client.getDB("gridtest");

        GridFS gridFS = new GridFS(db, "myfiles");

        GridFSTest.save(gridFS);
        GridFSTest.retrieve(gridFS);
    }

    private static void save(GridFS gridFS) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("video.mp4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BasicDBObject meta = new BasicDBObject();
        meta.put("tags", Arrays.asList("mongodb", "gridfs", "java"));
        meta.put("description", "MongoDB GridFS lesson");

        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, "gridfs.mp4");
        gridFSInputFile.setMetaData(meta);
        gridFSInputFile.setContentType("video/mp4");
        gridFSInputFile.save();

        System.out.println("Object ID in Files Collection " + gridFSInputFile.get("_id"));

        System.out.println("Saved this file to MongoDB");
    }

    private static void retrieve(GridFS gridFS) {
        System.out.println("Now lets read it back out");

        BasicDBObject video = new BasicDBObject("filename", "gridfs.mp4");

        GridFSDBFile gridFSDBFile = gridFS.findOne(video);

        try {
            FileOutputStream outputStream = new FileOutputStream("video_copy.mp4");
            gridFSDBFile.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Write the file back out");
    }
}
