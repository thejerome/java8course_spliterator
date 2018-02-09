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
        start = 0;
        end = list1.size() < list2.size()? list1.size() : list2.size();
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
        int startNew = start;
        int center = (end - start) / 2;
        start = center;
        return new ListZipSpliterator<>(list1, list2, combiner, startNew, center);
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
