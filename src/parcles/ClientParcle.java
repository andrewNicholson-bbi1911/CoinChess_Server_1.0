package parcles;

import java.io.Serializable;

public class ClientParcle implements Serializable {

    public float joystickX;
    public float Ytransform;
    public float joystickY;
    public float startAngle;
    public float deltaTime;
    public int action;
    public String clientMessage;

    public ClientParcle(float joystickX, float Ytransform, float joystickY, float startAngle, float deltaTime, int action, String clientMessage){
        this.joystickX = joystickX;
        this.Ytransform = Ytransform;
        this.joystickY = joystickY;
        this.startAngle = startAngle;
        this.deltaTime = deltaTime;
        this.action = action;
        this.clientMessage = clientMessage;
    }
}
