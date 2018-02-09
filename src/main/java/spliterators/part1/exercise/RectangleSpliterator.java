package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
    private int startInnerInclusive;
//    private final int endInnerInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        if (endOuterExclusive - startInnerInclusive > 1) {

            int mid = (startOuterInclusive + endOuterExclusive) / 2;
            int startOuterInclusiveTmp = startOuterInclusive;
            startOuterInclusive = mid;

            return new RectangleSpliterator(array, startOuterInclusiveTmp, mid, 0);
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive < endOuterExclusive) {

            action.accept(array[startOuterInclusive][startInnerInclusive++]);

            if (startInnerInclusive == innerLength) {
            startInnerInclusive = 0;
            startOuterInclusive++;
            }
            return true;
        }
        return false;
    }
}
