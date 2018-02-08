package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private final int endOuterExclusive;
    private int startInnerInclusive;
    private final int endInnerExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0
                , array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(final int[][] array, final int startOuterInclusive
            , final int endOuterExclusive, final int startInnerInclusive
            , final int endInnerExclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED
                | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        this.endInnerExclusive = endInnerExclusive;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
        innerLength = array.length == 0 ? 0 : array[0].length;
    }

    @Override
    public OfInt trySplit() {
        final int mid = (endOuterExclusive + startOuterInclusive) / 2;
        final RectangleSpliterator res = new RectangleSpliterator(array, startOuterInclusive, mid
                , startInnerInclusive, endInnerExclusive);
        startOuterInclusive = mid;
        startInnerInclusive = 0;
        return res;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(final IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        } else {
            final int value = array[startOuterInclusive][startInnerInclusive++];
            action.accept(value);
            if (startInnerInclusive >= endInnerExclusive) {
                startOuterInclusive++;
                startInnerInclusive = 0;
            }
        }
        return true;
    }
}
