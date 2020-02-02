/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxIdx = 0;
        int maxDist = Integer.MIN_VALUE;

        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (String noun : nouns) {
                if (!noun.equals(nouns[i])) {
                    distance += wordNet.distance(nouns[i], noun);
                }
            }

            if (distance > maxDist) {
                maxDist = distance;
                maxIdx = i;
            }
        }

        return nouns[maxIdx];
    }

    // see test client below
    public static void main(String[] args) {

    }

}
