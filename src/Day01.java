void main() throws IOException {

    var pattern = Pattern.compile("\\d+");
    var pairs = Files.lines(Path.of("01.txt"))
            .map(line -> pattern.matcher(line).results().map(MatchResult::group).map(Integer::parseInt).toList())
            .toList();

    var l1 = pairs.stream().map(List::getFirst).sorted().toList();
    var l2 = pairs.stream().map(List::getLast).sorted().toList();

    var distance = 0;

    for (int i = 0; i < l1.size(); i++) {
        distance += Math.abs(l2.get(i) - l1.get(i));
    }

    System.out.println(distance);

    var similarity = 0;

    for (var p1 : l1) {
        similarity += (int) (p1 * l2.stream().filter(p1::equals).count());
    }

    System.out.println(similarity);
}
