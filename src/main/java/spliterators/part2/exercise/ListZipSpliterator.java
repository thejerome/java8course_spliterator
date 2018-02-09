package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private List<L> list1;
    private List<R> list2;
    private BiFunction<L, R, T> combiner;
    private int start;
    private int end;


    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.start = 0;
        this.end = Math.min(list1.size(), list2.size());
    }

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int start, int end) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (end > start) {
            action.accept(combiner.apply(list1.get(start), list2.get(start)));
            start++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        int mid = (end - start) / 2;
        int start2 = start;
        start = mid;
        return new ListZipSpliterator<>(list1, list2, combiner, start2, mid);
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
