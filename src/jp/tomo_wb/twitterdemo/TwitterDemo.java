
package jp.tomo_wb.twitterdemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import twitter4j.*;
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
    
    public static void main(String[] args) throws TwitterException {
        InputKeyToken(args[0]);
        Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();
        
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        Query query = new Query();
        
        query.setQuery("仙台市");
        query.setLang("ja");
        query.setCount(100);
        query.resultType(Query.RECENT);
        
        int num = 0;
        for (int i = 1; i <= 1; i++) {
            QueryResult result = twitter.search(query);
            System.out.println("ヒット数 : " + result.getTweets().size());
            System.out.println("ページ数 : " + new Integer(i).toString());
            
            for (Status status : result.getTweets()) {
                if(!status.isRetweet()){
                    System.out.println(num + " " + status.getCreatedAt().toString() +" @" + status.getUser().getScreenName() + ":" + status.getText());
                    System.out.println(status.getId() + " " + status.getUser().getName()+ " " + status.getUser().getId());
                    num++;
                }
            }
            if (result.hasNext()) {
                query = result.nextQuery();
            } else {
                break;
            }
        }
        
        /*
        TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
        twStream.addListener(new MyStatusListener());
        twStream.sample();
                */
    }
    
    static class MyStatusListener implements StatusListener {
        @Override
        public void onStatus(Status status) {
            Double lat = null;
            Double lng = null;
            String[] urls = null;
            String[] medias = null;
            
            GeoLocation location = status.getGeoLocation();
            if( location != null ){
                double dlat = location.getLatitude();
                double dlng = location.getLongitude();
                lat = dlat;
                lng = dlng;
            }
            long id = status.getId(); //. ツイートID
            String text = status.getText(); //. ツイート本文
            long userid = status.getUser().getId(); //. ユーザーID
            String username = status.getUser().getScreenName(); //. ユーザー表示名
            Date created = status.getCreatedAt(); //. ツイート日時
            
            //. ツイート本文にリンクURLが含まれていれば取り出す
            URLEntity[] uentitys = status.getURLEntities();
            if( uentitys != null && uentitys.length > 0 ){
            	List list = new ArrayList();
                for( int i = 0; i < uentitys.length; i ++ ){
                    URLEntity uentity = uentitys[i];
                    String expandedURL = uentity.getExpandedURL();
                    list.add( expandedURL );
                }
	        urls = ( String[] )list.toArray( new String[0] );
            }
            
            //. ツイート本文に画像／動画URLが含まれていれば取り出す
            MediaEntity[] mentitys = status.getMediaEntities();
            if( mentitys != null && mentitys.length > 0 ){
            	List list = new ArrayList();
                for( int i = 0; i < mentitys.length; i ++ ){
                    MediaEntity mentity = mentitys[i];
                    String expandedURL = mentity.getExpandedURL();
                    list.add( expandedURL );
                }
                medias = ( String[] )list.toArray( new String[0] );
            }

            //. 取り出した情報を表示する（以下では id, username, text のみ）
            System.out.println( "id = " + id + ", username = " + username + ", text = " + text );
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice sdn) {
            //System.out.println("onDeletionNotice.");
        }

        @Override
        public void onTrackLimitationNotice(int i) {
            //System.out.println("onTrackLimitationNotice.(" + i + ")");
        }

        @Override
        public void onScrubGeo(long l, long l1) {
            //System.out.println("onScrubGeo.(" + lat + ", " + lng + ")");
        }

        @Override
        public void onStallWarning(StallWarning sw) {
             // TODO Auto-generated method stub
        }

        @Override
        public void onException(Exception excptn) {
            //System.out.println("onException.");
        }
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
