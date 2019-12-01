package parcles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PlayerLogIn {

    private static String loginPHP = "action_login.php";
    private static String loginHashPHP = "action_loginHash.php";

    public static String GetIPandLVL(String mail, String password, boolean needHash){



        if(needHash)password = password.split("/needHash/")[1];
        String _url = "http://www.andbases73i24.club/" +(needHash==true?loginHashPHP:loginPHP)+ "?email="+mail+"&password="+password;
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer strBuf = new StringBuffer();
            String line;

            while ((line = in.readLine())!=null){
                strBuf.append(line);
            }
            in.close();
            return strBuf.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }
}
