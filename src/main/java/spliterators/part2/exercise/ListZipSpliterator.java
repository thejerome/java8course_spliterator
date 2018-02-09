package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T>  implements Spliterator<T> {

    private int start;
    private int end;
    private List<L> list1;
    private List<R> list2;
    private BiFunction<L, R, T> combiner;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.start = 0;
        this.end = list1.size() < list2.size() ? list1.size() : list2.size();

    }

    public ListZipSpliterator(int start, int end, List<L> list1, List<R> list2,
        BiFunction<L, R, T> combiner) {
        this.start = start;
        this.end = end;
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        //throw new UnsupportedOperationException();
        if (start < end) {
            action.accept(combiner.apply(list1.get(start), list2.get(start)));
            start++;
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        //throw new UnsupportedOperationException();
        int startNew = start;
        int mid = (end - start) / 2;
        start = mid;

        return new ListZipSpliterator<>(startNew, mid, list1, list2, combiner);
    }

    @Override
    public long estimateSize() {
        //TODO
        //throw new UnsupportedOperationException();
        return end - start;
    }

    @Override
    public int characteristics() {
        //TODO
        //throw new UnsupportedOperationException();
        return 0;
    }
}
