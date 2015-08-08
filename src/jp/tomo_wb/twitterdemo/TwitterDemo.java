
package jp.tomo_wb.twitterdemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author tomoya-m
 */
public class TwitterDemo {
    private static String CONSUMER_KEY = "";
    private static String CONSUMER_SECRET = "";
    private static String ACCESS_TOKEN = "";
    private static String ACCESS_TOKEN_SECRET = "";
    
    public static void main(String[] args) {
        InputKeyToken(args[0]);
        Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();
    }
    
    private static void InputKeyToken(String filename){
        File file = new File(filename);
        ArrayList<String> al = FileReader(file);
        int check = 0;
        for(int i = 0; i < al.size(); ++i){
            if(al.get(i).contains("CONSUMER_KEY:")){
                CONSUMER_KEY = al.get(i).replaceAll("CONSUMER_KEY:", "");
                ++check;
            }
            else if(al.get(i).contains("CONSUMER_SECRET:")){
                CONSUMER_SECRET = al.get(i).replaceAll("CONSUMER_SECRET:", "");
                check += 2;
            }
            else if(al.get(i).contains("ACCESS_TOKEN:")){
                ACCESS_TOKEN = al.get(i).replaceAll("ACCESS_TOKEN:", "");
                check += 4;
            }
            else if(al.get(i).contains("ACCESS_TOKEN_SECRET:")){
                ACCESS_TOKEN_SECRET = al.get(i).replaceAll("ACCESS_TOKEN_SECRET:", "");
                check += 8;
            }  
        }
        if(check != 15){
            System.err.println("ERROR:" + check);
            System.exit(1);
        }
    }
    
    private static ArrayList<String> FileReader(File file){
        ArrayList<String> TextsArray = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            while(str != null){
                TextsArray.add(str);
                str = br.readLine();
            }
            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        
        return TextsArray;
    }
}
