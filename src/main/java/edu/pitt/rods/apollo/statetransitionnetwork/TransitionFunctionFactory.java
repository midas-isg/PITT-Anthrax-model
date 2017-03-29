package edu.pitt.rods.apollo.statetransitionnetwork;

import java.lang.reflect.Constructor;
import java.util.List;

public class TransitionFunctionFactory {

    public static StateTransitionFunction createFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode, AbstractStateTransitionNetwork stn) {
        try {
            Class<?>[] types = new Class[]{String.class, IStateNode.class,
                IStateNode.class, AbstractStateTransitionNetwork.class};

            Constructor<?> constructor = c.getConstructor(types);
            Object[] params = {c.getName(), fromNode, toNode, stn};

            return (StateTransitionFunction) constructor.newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;

    }

    public static StateTransitionFunction createFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode, AbstractStateTransitionNetwork stn,
            List<InfluenzaStateTransitionNetwork> remoteStns, String[] array) {
        try {
            Class<?>[] types = new Class[]{String.class, IStateNode.class,
                IStateNode.class, InfluenzaStateTransitionNetwork.class,
                List.class, String[].class};

            Constructor<?> constructor = c.getConstructor(types);
            Object[] params = {c.getName(), fromNode, toNode, stn, remoteStns,
                array};

            return (StateTransitionFunction) constructor.newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;

    }
}
