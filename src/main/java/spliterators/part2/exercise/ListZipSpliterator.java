package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private List<L> list1;
    private List<R> list2;
    private BiFunction<L, R, T> combiner;
    private int startInclusive;
    private final int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        endExclusive = list1.size();
    }

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int startInclusive, int endExclusive) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //DONE
        //throw new UnsupportedOperationException();
        if (startInclusive >= endExclusive) {
            return false;
        }

        try {
            action.accept(combiner.apply(list1.get(startInclusive), list2.get(startInclusive++)));
        }
        catch(Exception e) {

        }
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        //DONE
        //throw new UnsupportedOperationException();
        final int length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }

        int newStart = startInclusive;
        int mid = startInclusive + (endExclusive - startInclusive) / 2;
        startInclusive = mid;

        return new ListZipSpliterator(list1, list2, combiner, newStart, mid);
    }

    @Override
    public long estimateSize() {
        //DONE
        //throw new UnsupportedOperationException();

        return endExclusive - startInclusive;
    }

    @Override
    public int characteristics() {
        //DONE
        //throw new UnsupportedOperationException();
        return list1.spliterator().characteristics();
    }
}
