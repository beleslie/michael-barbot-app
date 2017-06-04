package l.michaelbarbot;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * Created by lesli_000 on 5/30/2017.
 */

public class DrinkListener implements NodeMain {
    private java.lang.String name;

    public DrinkListener() {
        name = "/drink_status";
    }
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        // set up the subscriber
        final Subscriber<std_msgs.Int64> subscriber =
                connectedNode.newSubscriber(name, "std_msgs.Int64");

        // This CancellableLoop will be canceled automatically when the node shuts down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                // TODO: figure out the subscriber version of this code

                /*
                std_msgs.String str = subscriber.newMessage();
                str.setData("Hello world! " + sequenceNumber);
                subscriber.publish(str);
                sequenceNumber++;
                Thread.sleep(1000);
                */
            }
        });

        /*
        final Log log = connectedNode.getLog();
        Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("chatter", std_msgs.String._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
            @Override
            public void onNewMessage(std_msgs.String message) {
                log.info("I heard: \"" + message.getData() + "\"");
            }
        });
        */
    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }
}
