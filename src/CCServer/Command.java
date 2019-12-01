package CCServer;

public class  Command{

   public static String strartGame = "!S";
   public static String radiant = "R";
   public static String dare = "D";
   public static String turn = "T";
   public static String missTurn = "Ms";
   public static String endGame = "E";
   public static String win = "W!";
   public static String lose = "L!";
   public static String noScore ="0!";

   public static String error = "er";
   public static String notEnoughCoins = "nec";
   public static String impossibleAction = "ia";
   public static String wrongCoords = "wc";
   public static String busyCell = "bc";
   public static String wrongID = "wid";
   public static String giveUpReason="G";
   public static String disconnectReason="_D";
   public static String noConnectedReason="_NC!";

   public static String pose = "P";
   public static String move = "M";
   public static String shift = "S";
   public static String cast = "C";
   public static String attack = "A";

   public static String Diclaim = "rej";

   public static byte[] getCoordinates(String coords) throws Exception{
       String[] _coords = coords.split(";");
       if(_coords.length!=2) {
           throw new Exception();
       }
       byte[] b_coords = new byte[2];
       b_coords[0]=Byte.valueOf(_coords[0]);
       b_coords[1]=Byte.valueOf(_coords[1]);
       return b_coords;
   }

   public static String setCoords(byte[] coords){
       return String.valueOf(coords[0])+";"+String.valueOf(coords[1]);
   }



}
