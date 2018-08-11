import java.util.ArrayList;

import com.google.gson.GsonBuilder;

public class BlockChain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static int difficulty = 5;

	public static void main(String[] args) {

		// Test Hachage
		Block genesisBlock = new Block("Premier Block", "0");
		System.out.println("Hash du block 1 :" + genesisBlock.hash);

		Block secondBlock = new Block("Second Block", genesisBlock.hash);
		System.out.println("Hash du block 2 :" + secondBlock.hash);

		Block TroisiemeBlock = new Block("Troisième Block", secondBlock.hash);
		System.out.println("Hash du Block 3 :" + TroisiemeBlock.hash);

		// On ajoute des blocks à ArrayList
		blockchain.add(new Block("Premier Block", "0"));
		blockchain.add(new Block("Deuxième Block", blockchain.get(blockchain.size() - 1).hash));
		blockchain.add(new Block("Troisième Block", blockchain.get(blockchain.size() - 1).hash));

		// Test Minage
		blockchain.get(2).mineBlock(difficulty);

		System.out.println("\nBlockchain is Valid " + isChainValid());

		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println(blockchainJson);

	}

	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;

		// Boucle de vérification des hashages
		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);

			// comparaison des hashages du block actuel
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("les hashages du block actuel ne correspondent pas");
				return false;
			}

			// comparaison du block précédent avec le bloc actuel
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("les hashages précédents ne correspondent pas");
				return false;
			}
		}
		return true;
	}
}
