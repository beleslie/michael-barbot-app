package l.michaelbarbot;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import std_msgs.String;

/**
 * Created by lesli_000 on 5/30/2017.
 */

public class DrinkTalker implements NodeMain {
    private java.lang.String name;

    public DrinkTalker() {
        name = "/drink_order";
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        // set up the publisher

        final Publisher<DrinkOrder> publisher =
                connectedNode.newPublisher(name, DrinkOrder);
        // This CancellableLoop will be canceled automatically when the node shuts down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {
                sequenceNumber = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                std_msgs.String str = publisher.newMessage();
                str.setData("Hello world! " + sequenceNumber);
                publisher.publish(str);
                sequenceNumber++;
                Thread.sleep(1000);
            }
        });
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
