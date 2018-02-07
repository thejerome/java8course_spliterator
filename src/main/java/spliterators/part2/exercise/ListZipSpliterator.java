package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private List<L> l;
    private List<R> r;
    private int start;
    private int end;
    private BiFunction<L, R, T> combiner;


    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.l = list1;
        this.r = list2;
        this.combiner = combiner;
        start = 0;
        end = Math.min(l.size(), r.size());
    }

    private ListZipSpliterator(List<L> l, List<R> r, BiFunction<L, R, T> combiner, int start, int end) {
        this.l = l;
        this.r = r;
        this.combiner = combiner;
        this.start = start;
        this.end = end;
    }

    @Override

    public boolean tryAdvance(Consumer<? super T> action) {
        if (end > start) {
            action.accept(combiner.apply(l.get(start), r.get(start)));
            start++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        int startNew = start;
        int mid = (end - start) / 2;
        start = mid;
        return new ListZipSpliterator<>(l, r, combiner, startNew, mid);
    }

    @Override
    public long estimateSize() {
        return ((long) end) - start;
    }

    @Override
    public int characteristics() {
        return l.spliterator().characteristics();
    }
}