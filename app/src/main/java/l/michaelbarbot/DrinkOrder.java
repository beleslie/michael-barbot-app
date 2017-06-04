package l.michaelbarbot;

import org.ros.node.topic.Publisher;

import java.util.UUID;

import std_msgs.String;

/**
 * Created by lesli_000 on 6/4/2017.
 */

public class DrinkOrder {
    private java.lang.String command;
    private java.lang.String id;
    private java.lang.String type;
    private java.lang.String amount;

    private static final java.lang.String MAKE_ORDER = "make_order";
    private static final java.lang.String CANCEL_ORDER = "cancel_order";

    public DrinkOrder(java.lang.String type, java.lang.String amount) {
        command = MAKE_ORDER;
        id = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
    }

    public java.lang.String getCommand() {
        return command;
    }

    public java.lang.String getId() {
        return id;
    }

    public java.lang.String getType() {
        return type;
    }
    public java.lang.String getAmount() {
        return amount;
    }
}
