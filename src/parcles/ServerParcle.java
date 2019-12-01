package parcles;

import java.io.Serializable;


public class ServerParcle implements Serializable {
    public String serverMessage;
    public float playerHealth;
    public float enemyHealth;
    public Float[] playerTransformBounds;
    public Float[] enemyTransformBounds;
    public float[] playerBounds;
    public float[] enemyBounds;
    public int enemyAction;

    public ServerParcle(String serverMessage, float playerHealth, float[] playerTransformBounds/*x, y, z, angle*/, float enemyHealth, float[] enemyTransformBounds, int enemyAction){
        this.serverMessage = serverMessage;
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
        this.playerTransformBounds = new Float[]{playerTransformBounds[0], playerTransformBounds[1], playerTransformBounds[2], playerTransformBounds[3]};
        this.enemyTransformBounds = new Float[]{enemyTransformBounds[0], enemyTransformBounds[1], enemyTransformBounds[2], enemyTransformBounds[3]};;
        this.enemyAction = enemyAction;
    }

    public void tofloat(){
        playerBounds =new float[] {playerTransformBounds[0],
                playerTransformBounds[1],
                playerTransformBounds[2],
                playerTransformBounds[3]} ;
        enemyBounds =new float[] {enemyTransformBounds[0],
                enemyTransformBounds[1],
                enemyTransformBounds[2],
                enemyTransformBounds[3]} ;
    }
}