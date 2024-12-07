void main() throws IOException {

    var reports = Files.lines(Path.of("2.txt"))
            .map(line -> line.split(" "))
            .map(arr -> Arrays.stream(arr).map(Integer::parseInt).toList())
            .toList();

    System.out.println(reports.stream().filter(this::isSafe1).count());
    System.out.println(reports.stream().filter(this::isSafe2).count());
}

boolean isSafe1(List<Integer> l) {
    if (l.size() == 0 || l.size() == 1) {
        return true;
    }

    var ascending = l.get(0) < l.get(1);

    if (ascending) {
        for (int i = 0; i < l.size() - 1; i++) {
            if (l.get(i) >= l.get(i + 1) || Math.abs(l.get(i) - l.get(i + 1)) > 3) {
                return false;
            }
        }
    } else {
        for (int i = 0; i < l.size() - 1; i++) {
            if (l.get(i) <= l.get(i + 1) || Math.abs(l.get(i) - l.get(i + 1)) > 3) {
                return false;
            }
        }
    }

    return true;
}

boolean isSafe2(List<Integer> l) {
    if (l.isEmpty()) {
        return true;
    }

    return IntStream.range(0, l.size()).mapToObj(i -> {
        var t = new ArrayList<>(l);
        t.remove(i);
        return t;
    }).anyMatch(this::isSafe1);
}
