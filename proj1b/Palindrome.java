public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> res = new ArrayDeque<>();
        char[] c = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            res.addLast(c[i]);
        }
        return res;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> wordList = wordToDeque(word);

        for (int i = 0; i < word.length() / 2; i++) {
            if (!((Character) word.charAt(i)).equals(wordList.removeLast())) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordlist = wordToDeque(word);
        for (int i = 0; i < word.length() / 2; i++) {
            if (!cc.equalChars(word.charAt(i), wordlist.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
