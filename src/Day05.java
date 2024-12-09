void main() throws IOException {

    var lines = Files.readAllLines(Path.of("05.txt"));
    var ordering = true;
    var rules = new ArrayList<List<Integer>>();
    var pages = new ArrayList<List<Integer>>();

    for (var line : lines) {
        if (line.isBlank()) {
            ordering = false;
            continue;
        }

        if (ordering) {
            rules.add(Arrays.stream(line.split("\\|")).map(Integer::parseInt).toList());
        } else {
            pages.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
        }
    }

    System.out.println(pages.stream()
            .filter(p -> isOrdered(rules, p))
            .mapToInt(p -> p.get(p.size() / 2))
            .sum());

    System.out.println(pages.stream()
            .filter(p -> !isOrdered(rules, p))
            .map(p -> order(rules, p))
            .mapToInt(p -> p.get(p.size() / 2))
            .sum());
}

boolean isOrdered(List<List<Integer>> rules, List<Integer> pages) {

    for (int i = 0; i < pages.size(); i++) {
        var page = pages.get(i);

        var before = rules.stream()
                .filter(p -> page.equals(p.get(1)))
                .map(p -> p.get(0))
                .toList();

        var after = pages.subList(i + 1, pages.size());

        if (after.stream().anyMatch(before::contains)) {
            return false;
        }
    }

    return true;
}

List<Integer> order(List<List<Integer>> rules, List<Integer> pages) {

    var r = new ArrayList<>(pages);

    outer:
    for (int i = 0; i < r.size(); i++) {
        var t = r.get(i);

        var before = rules.stream()
                .filter(p -> t.equals(p.get(1)))
                .map(p -> p.get(0))
                .toList();

        for (int j = i + 1; j < r.size(); j++) {
            if (before.contains(r.get(j))) {
                r.set(i, r.get(j));
                r.set(j, t);
                i = -1;
                continue outer;
            }
        }
    }

    return r;
}
