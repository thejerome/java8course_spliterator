package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;
    private int startInclusive;
    private int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this(list1, list2, combiner, 0, Math.min(list1.size(), list2.size()));
    }

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner,
                              int startInclusive, int endExclusive) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;

    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (startInclusive < endExclusive) {
            action.accept(combiner.apply(list1.get(startInclusive), list2.get(startInclusive)));
            startInclusive++;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        int mid = startInclusive + (endExclusive - startInclusive) / 2;

        ListZipSpliterator listZipSpliterator = new ListZipSpliterator<>(list1, list2, combiner, startInclusive, mid);
        startInclusive = mid;
        return listZipSpliterator;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
