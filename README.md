# clojure.data.priority-map

Formerly clojure.contrib.priority-map.

A priority map is very similar to a sorted map,
but whereas a sorted map produces a
sequence of the entries sorted by key, a priority
map produces the entries sorted by value.

In addition to supporting all the functions a
sorted map supports, a priority map
can also be thought of as a queue of [item priority] pairs.
To support usage as a versatile priority queue,
priority maps also support conj/peek/pop operations.

## Releases and Dependency Information

Latest stable release is [0.0.2]

The README below also describes a new constructor `priority-map-keyfn`
which is available in [0.0.3-SNAPSHOT].

[Leiningen](https://github.com/technomancy/leiningen) dependency information:

    [org.clojure/data.priority-map "0.0.2"]

[Maven](http://maven.apache.org/) dependency information:

    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>data.priority-map</artifactId>
      <version>0.0.2</version>
    </dependency>


## Usage

The standard way to construct a priority map is with priority-map:

    user=> (def p (priority-map :a 2 :b 1 :c 3 :d 5 :e 4 :f 3))
    #'user/p
    
    user=> p
    {:b 1, :a 2, :c 3, :f 3, :e 4, :d 5}

So :b has priority 1, :a has priority 2, and so on.
Notice how the priority map prints in an order sorted by its priorities (i.e., the map's values)

We can use assoc to assign a priority to a new item:

    user=> (assoc p :g 1)
    {:b 1, :g 1, :a 2, :c 3, :f 3, :e 4, :d 5}

or to assign a new priority to an extant item:

    user=> (assoc p :c 4)
    {:b 1, :a 2, :f 3, :c 4, :e 4, :d 5}

We can remove an item from the priority map:

    user=> (dissoc p :e)
    {:b 1, :a 2, :c 3, :f 3, :d 5}

An alternative way to add to the priority map is to conj a [item priority] pair:

    user=> (conj p [:g 0])
    {:g 0, :b 1, :a 2, :c 3, :f 3, :e 4, :d 5}

or use into:

    user=> (into p [[:g 0] [:h 1] [:i 2]])
    {:g 0, :b 1, :h 1, :a 2, :i 2, :c 3, :f 3, :e 4, :d 5}

Priority maps are countable:

    user=> (count p)
    6

Like other maps, equivalence is based not on type, but on contents.
In other words, just as a sorted-map can be equal to a hash-map,
so can a priority-map.

    user=> (= p {:b 1, :a 2, :c 3, :f 3, :e 4, :d 5})
    true

You can test them for emptiness:

    user=> (empty? (priority-map))
    true

    user=> (empty? p)
    false

You can test whether an item is in the priority map:

    user=> (contains? p :a)
    true
    
    user=> (contains? p :g)
    false

It is easy to look up the priority of a given item, using any of the standard map mechanisms:

    user=> (get p :a)
    2

    user=> (get p :g 10)
    10

    user=> (p :a)
    2
    
    user=> (:a p)
    2

Priority maps derive much of their utility by providing priority-based seq.
Note that no guarantees are made about the order in which items of the same priority appear.

    user=> (seq p)
    ([:b 1] [:a 2] [:c 3] [:f 3] [:e 4] [:d 5])

Because no guarantees are made about the order of same-priority items, note that
rseq might not be an exact reverse of the seq.  It is only guaranteed to be in
descending order.

    user=> (rseq p)
    ([:d 5] [:e 4] [:c 3] [:f 3] [:a 2] [:b 1])

This means first/rest/next/for/map/etc. all operate in priority order.

    user=> (first p)
    [:b 1]
    
    user=> (rest p)
    ([:a 2] [:c 3] [:f 3] [:e 4] [:d 5])

Priority maps support metadata:

    user=> (meta (with-meta p {:extra :info}))
    {:extra :info}

But perhaps most importantly, priority maps can also function as priority queues.
peek, like first, gives you the first [item priority] pair in the collection.
pop removes the first [item priority] from the collection.
(Note that unlike rest, which returns a seq, pop returns a priority map).

    user=> (peek p)
    [:b 1]
    
    user=> (pop p)
    {:a 2, :c 3, :f 3, :e 4, :d 5}

It is also possible to use a custom comparator:

    user=> (priority-map-by (comparator >) :a 1 :b 2 :c 3)
    {:c 3, :b 2, :a 1}

Sometimes, it is desirable to have a map where the values contain more information
than just the priority.  For example, let's say you want a map like:

    {:a [2 :apple], :b [1 :banana], :c [3 :carrot]}

and you want to sort the map by the numeric priority found in the pair.

A common mistake is to try to solve this with a custom comparator:

    (priority-map 
      (fn [[priority1 _] [priority2 _]] (< priority1 priority2))
      :a [2 :apple], :b [1 :banana], :c [3 :carrot])

This will not work!  In Clojure, like Java, all comparators must be *total orders*,
meaning that you can't have a "tie" unless the objects you are comparing are
in fact equal.  The above comparator breaks that rule because
[2 :apple] and [2 :apricot] tie, but are not equal.

The correct way to construct such a priority map is by specifying a keyfn
(similar to the way clojure.core/sort-by takes a keyfn), which is used
to extract the true priority from the priority map's vals.  In the above example,

    user=> (priority-map-keyfn first :a [2 :apple], :b [1 :banana], :c [3 :carrot])
    {:b [1 :banana], :a [2 :apple], :c [3 :carrot]}

You can also combine a keyfn with a comparator that operates on the extracted priorities:

    user=> (priority-map-keyfn first > :a [2 :apple], :b [1 :banana], :c [3 :carrot])
    {:c [3 :carrot], :a [2 :apple], :b [1 :banana]}

## License

Copyright (C) 2013 Mark Engelberg

Distributed under the Eclipse Public License, the same as Clojure.
