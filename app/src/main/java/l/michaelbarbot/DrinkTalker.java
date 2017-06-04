package l.michaelbarbot;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lesli_000 on 5/30/2017.
 */

public class DrinkTalker implements NodeMain {
    private java.lang.String name;
    private Queue<DrinkOrder> queue;

    public DrinkTalker() {
        queue = new ConcurrentLinkedQueue<>();
        name = "/drink_order";
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        // set up the publisher
        final Publisher<std_msgs.String> publisher =
                connectedNode.newPublisher(name, "std_msgs.String");

        // This CancellableLoop will be canceled automatically when the node shuts down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                if (!queue.isEmpty()) {
                    DrinkOrder order = queue.remove();

                    // publish the command
                    std_msgs.String command = publisher.newMessage();
                    command.setData(order.getCommand());
                    publisher.publish(command);

                    // publish the id
                    std_msgs.String id = publisher.newMessage();
                    id.setData(order.getId());
                    publisher.publish(id);

                    // publish the type
                    std_msgs.String type = publisher.newMessage();
                    type.setData(order.getType());
                    publisher.publish(type);

                    // publish the amount
                    std_msgs.String amount = publisher.newMessage();
                    amount.setData(order.getAmount());
                    publisher.publish(amount);
                }
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

    public boolean orderDrink(java.lang.String type, java.lang.String amount) {
        return queue.add(new DrinkOrder(type, amount));
    }
}
