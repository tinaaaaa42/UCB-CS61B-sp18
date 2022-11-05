public class OffByN implements CharacterComparator {
    private int N;
    public OffByN(int n) {
        this.N = n;
    }
    @Override
    public boolean equalChars(char x, char y) {
        return java.lang.Math.abs(x - y) == N;
    }
}
