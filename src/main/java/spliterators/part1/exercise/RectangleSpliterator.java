package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int innerLength;
    private final int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
    private int startInnerInclusive;
    private long counter;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive, int innerLength) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        this.innerLength = innerLength;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        if ((endOuterExclusive - startOuterInclusive) >= innerLength) {
            int outerMiddle = (endOuterExclusive + startOuterInclusive)/2;
            final RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(
                    array,
                    outerMiddle,
                    endOuterExclusive,
                    startInnerInclusive,
                    innerLength
            );
            endOuterExclusive = outerMiddle;
            return rectangleSpliterator;
        } else {
            int newInnerLength = innerLength/2;
            final RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(
                    array,
                    startOuterInclusive,
                    endOuterExclusive,
                    startInnerInclusive + newInnerLength,
                    innerLength - newInnerLength
            );
            innerLength = newInnerLength;
            return rectangleSpliterator;
        }
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - counter;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (counter >= (((long) endOuterExclusive - startOuterInclusive)*innerLength)) {
            return false;
        }
        final int outer = (int) counter / innerLength + startOuterInclusive;
        final int inner = (int)(counter - ((int) counter / innerLength) * innerLength + startInnerInclusive);
        action.accept(array[outer][inner]);
        counter++;
        return true;
    }
}
