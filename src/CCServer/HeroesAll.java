package CCServer;

import spells.Howl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class HeroesAll {
    private static ArrayList<HeroClass> heroesBase = new ArrayList<>();
    boolean libraryDownloaded = false;
    void getHeroes(){
        System.out.println("started");
        for (int a = 2; a >= 1; a--) {//getting heroes with length of code = 3
            for (int b = 2; b >= 1; b--) {
                for (int c = 2; c >= 1; c--) {
                    takeHeroFromDB(100*a+10*b+c);
                    System.out.println("found: "+ (100*a+10*b+c));
                }
            }
        }
        for (int a = 2; a >= 1; a--) {//getting heroes with length of code = 2
            for (int b = 2; b >= 1; b--) {
                takeHeroFromDB(10*a+b);
                System.out.println("found: "+ (10*a+b));
            }
        }
        for (int a = 2; a > 0; a--) {//getting heroes with length of code = 1
            takeHeroFromDB(a);

            System.out.println("found: "+ a);
        }
        libraryDownloaded = true;
    }

    private void takeHeroFromDB(int id){
        try {
            String _url = "http://www.andbases73i24.club/getHeroInfoServer.php?id=";
            URL url = new URL(_url + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder strBuf = new StringBuilder();
            String line;

            while ((line = in.readLine())!=null){
                strBuf.append(line);
            }
            in.close();



         heroesBase.add(new HeroClass(strBuf.toString().split(":"), null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ArrayList<HeroClass> getHeroesBase(){
        return heroesBase;
    }

}


