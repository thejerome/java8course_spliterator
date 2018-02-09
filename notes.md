####1

```java
action.accept(combiner.apply(list1.get(start), list2.get(start)));
start++;
```
vs
```java
action.accept(combiner.apply(list1.get(start), list2.get(start++)));
```

####2

```java
public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
    initValues = IntStream.range(0, Math.min(list1.size(), list2.size()))
    .mapToObj(i -> combiner.apply(list1.get(i), list2.get(i)))
    .collect(Collectors.toList());
}
```
####3
```java
        ...
        this.listIterator1 = list1.listIterator();
        this.listIterator2 = list2.listIterator();
        ...
        
        ...
        new ListZipSpliterator<>(
                list1.subList(0, list1.size()/2),
                list2.subList(0, list2.size()/2),
                combiner,
                listIterator1,
                listIterator2
        );
        ...
```