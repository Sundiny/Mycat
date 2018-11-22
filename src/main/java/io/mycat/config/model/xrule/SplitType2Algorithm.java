package io.mycat.config.model.xrule;

import io.mycat.route.function.AbstractPartitionAlgorithm;
import io.mycat.route.function.AutoPartitionByLong;
import io.mycat.route.function.PartitionByMod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xueguangshun on 26/08/2018.
 */
public class SplitType2Algorithm {
    private final static Map<SplitType, Class<? extends AbstractPartitionAlgorithm>> splitType2PartitionAlg = new HashMap<>();

    static {
        splitType2PartitionAlg.put(SplitType.HASH, PartitionByMod.class);
        splitType2PartitionAlg.put(SplitType.RANGE, AutoPartitionByLong.class);
    }

    public static Class<? extends AbstractPartitionAlgorithm> getBySplitType(SplitType type){
        return splitType2PartitionAlg.get(type);
    }
}
